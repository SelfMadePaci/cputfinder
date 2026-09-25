const API_BASE_URL = "http://localhost:8080/api";

const loginQuery = new URLSearchParams(window.location.search);
if (loginQuery.get("registered") === "true") {
  showFormStatus("loginStatus", "Registration successful. You can now log in.");
}

function showFormStatus(elementId, message, isError = false) {
  const status = document.getElementById(elementId);
  if (!status) {
    return;
  }

  status.textContent = message;
  status.classList.toggle("error", isError);
  status.classList.toggle("success", !isError && Boolean(message));
}

function getApiErrorMessage(result, fallback = "The request could not be completed.") {
  if (typeof result === "string" && result.trim()) {
    return result;
  }
  if (result && typeof result === "object") {
    if (typeof result.message === "string" && result.message.trim()) return result.message;
    if (typeof result.error === "string" && result.error.trim()) return result.error;
    if (typeof result.detail === "string" && result.detail.trim()) return result.detail;
  }
  return fallback;
}

async function sendApiRequest(endpoint, payload) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: payload ? "POST" : "GET",
    headers: {
      "Content-Type": "application/json"
    },
    ...(payload ? { body: JSON.stringify(payload) } : {})
  });

  let result;
  try {
    result = await response.json();
  } catch {
    throw new Error("The server returned an invalid response.");
  }

  if (!response.ok) {
    throw new Error(getApiErrorMessage(result));
  }

  return result;
}

async function sendStudentRequest(endpoint, method, payload) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      "X-Admin-Session": localStorage.getItem("adminSession") || "",
      "X-Student-Id": localStorage.getItem("studentId") || ""
    },
    ...(payload ? { body: JSON.stringify(payload) } : {})
  });
  if (response.status === 204) return null;
  const result = await response.json();
  if (!response.ok) throw new Error(getApiErrorMessage(result));
  return result;
}

const studentForm = document.getElementById("studentForm");
if (studentForm) {
  if (localStorage.getItem("userRole") !== "ADMIN" || !localStorage.getItem("adminSession")) {
    window.location.href = "index.html";
  }
  let students = [];
  const rows = document.getElementById("studentRows");
  const search = document.getElementById("studentSearch");
  const status = message => showFormStatus("studentStatus", message, false);
  const error = message => showFormStatus("studentStatus", message, true);

  function renderStudents() {
    const query = search.value.trim().toLowerCase();
    rows.innerHTML = students
      .filter(student => [student.studentNumber, student.fullName, student.studentEmail]
        .some(value => value.toLowerCase().includes(query)))
      .map(student => `<tr>
        <td>${student.studentNumber}</td><td>${student.fullName}</td><td>${student.studentEmail}</td>
        <td><button type="button" class="table-button" data-edit="${student.studentId}">Edit</button>
        <button type="button" class="table-button danger" data-delete="${student.studentId}">Delete</button></td>
      </tr>`).join("");
  }

  async function loadStudents() {
    try {
      students = await sendStudentRequest("/students", "GET");
      renderStudents();
    } catch (e) {
      error(e instanceof TypeError ? "Cannot connect to the Spring Boot API." : e.message);
    }
  }

  studentForm.addEventListener("submit", async event => {
    event.preventDefault();
    const id = document.getElementById("studentId").value;
    const payload = {
      studentNumber: document.getElementById("recordNumber").value.trim(),
      fullName: document.getElementById("recordName").value.trim(),
      studentEmail: document.getElementById("recordEmail").value.trim(),
      password: document.getElementById("recordPassword").value
    };
    try {
      await sendStudentRequest(id ? `/students/${id}` : "/students", id ? "PUT" : "POST", payload);
      studentForm.reset();
      document.getElementById("studentId").value = "";
      document.getElementById("studentSubmit").textContent = "Create student";
      document.getElementById("cancelEdit").hidden = true;
      status(id ? "Student updated successfully." : "Student created successfully.");
      await loadStudents();
    } catch (e) {
      error(e instanceof TypeError ? "Cannot connect to the Spring Boot API." : e.message);
    }
  });

  rows.addEventListener("click", async event => {
    const editId = event.target.dataset.edit;
    const deleteId = event.target.dataset.delete;
    if (editId) {
      const student = students.find(item => item.studentId === Number(editId));
      document.getElementById("studentId").value = student.studentId;
      document.getElementById("recordNumber").value = student.studentNumber;
      document.getElementById("recordName").value = student.fullName;
      document.getElementById("recordEmail").value = student.studentEmail;
      document.getElementById("recordPassword").value = "";
      document.getElementById("studentSubmit").textContent = "Update student";
      document.getElementById("cancelEdit").hidden = false;
    } else if (deleteId && confirm("Delete this student record?")) {
      try {
        await sendStudentRequest(`/students/${deleteId}`, "DELETE");
        status("Student deleted successfully.");
        await loadStudents();
      } catch (e) {
        error(e instanceof TypeError ? "Cannot connect to the Spring Boot API." : e.message);
      }
    }
  });

  document.getElementById("cancelEdit").addEventListener("click", () => {
    studentForm.reset();
    document.getElementById("studentId").value = "";
    document.getElementById("studentSubmit").textContent = "Create student";
    document.getElementById("cancelEdit").hidden = true;
  });
  search.addEventListener("input", renderStudents);
  loadStudents();
}

const buildingForm = document.getElementById("buildingForm");
const roomForm = document.getElementById("roomForm");
if (buildingForm || roomForm) {
  if (localStorage.getItem("userRole") !== "ADMIN" || !localStorage.getItem("adminSession")) {
    window.location.href = "index.html";
  }

  if (roomForm) {
    if (localStorage.getItem("userRole") !== "ADMIN" || !localStorage.getItem("adminSession")) window.location.href = "index.html";
    let rooms = [], buildingsForRooms = [];
    const roomRows = document.getElementById("roomRows");
    const roomSearch = document.getElementById("roomSearch");
    const roomMessage = (message, isError = false) => showFormStatus("roomStatus", message, isError);
    function renderRooms() {
      const query = roomSearch.value.trim().toLowerCase();
      roomRows.innerHTML = rooms.filter(room =>
        `${room.roomNumber} ${room.buildingName}`.toLowerCase().includes(query))
        .map(room => `<tr><td>${room.roomNumber}</td><td>${room.floorNumber}</td><td>${room.buildingName || "Unknown"}</td>
          <td><button type="button" class="table-button" data-room-edit="${room.roomId}">Edit</button>
          <button type="button" class="table-button danger" data-room-delete="${room.roomId}">Delete</button></td></tr>`).join("");
    }

    function adminOnly() {
      if (localStorage.getItem("userRole") !== "ADMIN" || !localStorage.getItem("adminSession")) window.location.href = "index.html";
    }

    const lecturerForm = document.getElementById("lecturerForm");
    if (lecturerForm) {
      adminOnly();
      let lecturers = [];
      const rows = document.getElementById("lecturerRows");
      const search = document.getElementById("lecturerSearch");
      const message = (text, error = false) => showFormStatus("lecturerStatus", text, error);
      const render = () => {
        const q = search.value.toLowerCase().trim();
        rows.innerHTML = lecturers.filter(l => `${l.firstName} ${l.lastName} ${l.email}`.toLowerCase().includes(q)).map(l =>
          `<tr><td>${l.firstName} ${l.lastName}</td><td>${l.email}</td><td><button type="button" class="table-button" data-lecturer-edit="${l.lecturerId}">Edit</button> <button type="button" class="table-button danger" data-lecturer-delete="${l.lecturerId}">Delete</button></td></tr>`).join("");
      };
      const load = async () => { try { lecturers = await sendStudentRequest("/lecturers", "GET"); render(); } catch (e) { message(e.message, true); } };
      lecturerForm.addEventListener("submit", async e => {
        e.preventDefault(); const id = document.getElementById("lecturerId").value;
        const data = { firstName: lecturerFirst.value.trim(), lastName: lecturerLast.value.trim(), email: lecturerEmail.value.trim(), officeId: 0 };
        try { await sendStudentRequest(id ? `/lecturers/${id}` : "/lecturers", id ? "PUT" : "POST", data); lecturerForm.reset(); lecturerId.value = ""; lecturerSubmit.textContent = "Create lecturer"; cancelLecturer.hidden = true; message(id ? "Lecturer updated successfully." : "Lecturer created successfully."); await load(); } catch (e) { message(e.message, true); }
      });
      rows.addEventListener("click", async e => {
        const edit = e.target.dataset.lecturerEdit, del = e.target.dataset.lecturerDelete;
        if (edit) { const l = lecturers.find(x => x.lecturerId === Number(edit)); lecturerId.value = l.lecturerId; lecturerFirst.value = l.firstName; lecturerLast.value = l.lastName; lecturerEmail.value = l.email; lecturerSubmit.textContent = "Update lecturer"; cancelLecturer.hidden = false; }
        else if (del && confirm("Delete this lecturer record?")) { try { await sendStudentRequest(`/lecturers/${del}`, "DELETE"); message("Lecturer deleted successfully."); await load(); } catch (e) { message(e.message, true); } }
      });
      cancelLecturer.onclick = () => { lecturerForm.reset(); lecturerId.value = ""; lecturerSubmit.textContent = "Create lecturer"; cancelLecturer.hidden = true; };
      search.oninput = render; load();
    }

    const courseForm = document.getElementById("courseForm");
    if (courseForm) {
      adminOnly();
      let courses = [], lecturersForCourses = [];
      const rows = document.getElementById("courseRows"), search = document.getElementById("courseSearch");
      const message = (text, error = false) => showFormStatus("courseStatus", text, error);
      const render = () => {
        const q = search.value.toLowerCase().trim();
        rows.innerHTML = courses.filter(c => `${c.courseCode} ${c.courseName} ${c.lecturerName || ""}`.toLowerCase().includes(q)).map(c =>
          `<tr><td>${c.courseCode}</td><td>${c.courseName}</td><td>${c.lecturerName || "Unassigned"}</td><td><button type="button" class="table-button" data-course-edit="${c.courseId}">Edit</button> <button type="button" class="table-button danger" data-course-delete="${c.courseId}">Delete</button></td></tr>`).join("");
      };
      const load = async () => { try { courses = await sendStudentRequest("/courses", "GET"); render(); } catch (e) { message(e.message, true); } };
      const loadLecturers = async () => { lecturersForCourses = await sendStudentRequest("/lecturers", "GET"); courseLecturer.innerHTML = lecturersForCourses.map(l => `<option value="${l.lecturerId}">${l.firstName} ${l.lastName}</option>`).join(""); };
      courseForm.addEventListener("submit", async e => {
        e.preventDefault(); const id = courseId.value;
        const data = { courseCode: courseCode.value.trim(), courseName: courseName.value.trim(), lecturerId: Number(courseLecturer.value) };
        try { await sendStudentRequest(id ? `/courses/${id}` : "/courses", id ? "PUT" : "POST", data); courseForm.reset(); courseId.value = ""; courseSubmit.textContent = "Create course"; cancelCourse.hidden = true; message(id ? "Course updated successfully." : "Course created successfully."); await load(); } catch (e) { message(e.message, true); }
      });
      rows.addEventListener("click", async e => {
        const edit = e.target.dataset.courseEdit, del = e.target.dataset.courseDelete;
        if (edit) { const c = courses.find(x => x.courseId === Number(edit)); courseId.value = c.courseId; courseCode.value = c.courseCode; courseName.value = c.courseName; courseLecturer.value = c.lecturerId; courseSubmit.textContent = "Update course"; cancelCourse.hidden = false; }
        else if (del && confirm("Delete this course record?")) { try { await sendStudentRequest(`/courses/${del}`, "DELETE"); message("Course deleted successfully."); await load(); } catch (e) { message(e.message, true); } }
      });
      cancelCourse.onclick = () => { courseForm.reset(); courseId.value = ""; courseSubmit.textContent = "Create course"; cancelCourse.hidden = true; };
      search.oninput = render; loadLecturers().then(load).catch(e => message(e.message, true));
    }

    const foodStoreForm = document.getElementById("foodStoreForm");
    if (foodStoreForm) {
      adminOnly();
      let stores = [], storeBuildings = [];
      const rows = document.getElementById("foodStoreRows"), search = document.getElementById("foodStoreSearch");
      const message = (text, error = false) => showFormStatus("foodStoreStatus", text, error);
      const render = () => {
        const q = search.value.toLowerCase().trim();
        rows.innerHTML = stores.filter(s => `${s.storeName} ${s.foodType} ${s.buildingName}`.toLowerCase().includes(q)).map(s =>
          `<tr><td>${s.storeName}</td><td>${s.operatingHours}</td><td>${s.foodType}</td><td>${s.buildingName || "Unknown"}</td><td><button type="button" class="table-button" data-store-edit="${s.storeId}">Edit</button> <button type="button" class="table-button danger" data-store-delete="${s.storeId}">Delete</button></td></tr>`).join("");
      };
      const load = async () => { try { stores = await sendStudentRequest("/food-stores", "GET"); render(); } catch (e) { message(e.message, true); } };
      const loadBuildings = async () => { storeBuildings = await sendStudentRequest("/buildings", "GET"); foodStoreBuilding.innerHTML = storeBuildings.map(b => `<option value="${b.buildingId}">${b.buildingName} (${b.buildingCode})</option>`).join(""); };
      foodStoreForm.addEventListener("submit", async e => {
        e.preventDefault(); const id = foodStoreId.value;
        const data = { storeName: foodStoreName.value.trim(), operatingHours: foodStoreHours.value.trim(), foodType: foodStoreType.value.trim(), buildingId: Number(foodStoreBuilding.value) };
        try { await sendStudentRequest(id ? `/food-stores/${id}` : "/food-stores", id ? "PUT" : "POST", data); foodStoreForm.reset(); foodStoreId.value = ""; foodStoreSubmit.textContent = "Create food store"; cancelFoodStore.hidden = true; message(id ? "Food store updated successfully." : "Food store created successfully."); await load(); } catch (e) { message(e.message, true); }
      });
      rows.addEventListener("click", async e => {
        const edit = e.target.dataset.storeEdit, del = e.target.dataset.storeDelete;
        if (edit) { const s = stores.find(x => x.storeId === Number(edit)); foodStoreId.value = s.storeId; foodStoreName.value = s.storeName; foodStoreHours.value = s.operatingHours; foodStoreType.value = s.foodType; foodStoreBuilding.value = s.buildingId; foodStoreSubmit.textContent = "Update food store"; cancelFoodStore.hidden = false; }
        else if (del && confirm("Delete this food store record?")) { try { await sendStudentRequest(`/food-stores/${del}`, "DELETE"); message("Food store deleted successfully."); await load(); } catch (e) { message(e.message, true); } }
      });
      cancelFoodStore.onclick = () => { foodStoreForm.reset(); foodStoreId.value = ""; foodStoreSubmit.textContent = "Create food store"; cancelFoodStore.hidden = true; };
      search.oninput = render; loadBuildings().then(load).catch(e => message(e.message, true));
    }

    const timetableRows = document.getElementById("timetableRows");
    if (timetableRows) {
      const studentId = localStorage.getItem("studentId");
      const timetableStatus = document.getElementById("timetableStatus");
      if (!studentId) {
        timetableStatus.textContent = "Log in as a student to view your timetable.";
        timetableRows.innerHTML = "";
      } else {
        fetch(`${API_BASE_URL}/schedules/student/${studentId}`, {
          headers: { "X-Student-Id": studentId }
        })
          .then(async response => {
            const records = await response.json();
            if (!response.ok) throw new Error(records.message || "Timetable could not be loaded.");
            return records;
          })
          .then(records => {
            timetableRows.innerHTML = records.length
              ? records.map(schedule => `<tr><td>${schedule.classDate}</td><td>${schedule.startingTime} - ${schedule.endingTime}</td><td>${schedule.moduleCode || "Course"}</td><td>${schedule.roomNumber || "Room"}</td></tr>`).join("")
              : "<tr><td colspan='4'>No timetable records found.</td></tr>";
          })
          .catch(error => {
            timetableStatus.textContent = error instanceof TypeError
              ? "Cannot connect to the Spring Boot API."
              : error.message;
            timetableStatus.classList.add("error");
            timetableRows.innerHTML = "";
          });
      }

      const scheduleForm = document.getElementById("scheduleForm");
      if (scheduleForm) {
        adminOnly();
        let schedules = [];
        const scheduleRows = document.getElementById("scheduleRows");
        const message = (text, error = false) => showFormStatus("scheduleStatus", text, error);
        const render = () => {
          scheduleRows.innerHTML = schedules.map(s => `<tr><td>${s.classDate}</td><td>${s.startingTime} - ${s.endingTime}</td><td>${s.moduleCode || "Course"}</td><td>${s.roomNumber || "Room"}</td><td><button type="button" class="table-button" data-schedule-edit="${s.scheduleId}">Edit</button> <button type="button" class="table-button danger" data-schedule-delete="${s.scheduleId}">Delete</button></td></tr>`).join("");
        };
        const load = async () => { schedules = await sendStudentRequest("/schedules", "GET"); render(); };
        const loadOptions = async () => {
          const [students, courses, rooms] = await Promise.all([
            sendStudentRequest("/students", "GET"), sendStudentRequest("/courses", "GET"), sendStudentRequest("/rooms", "GET")
          ]);
          scheduleStudent.innerHTML = students.map(s => `<option value="${s.studentId}">${s.fullName} (${s.studentNumber})</option>`).join("");
          scheduleCourse.innerHTML = courses.map(c => `<option value="${c.courseId}">${c.courseCode} - ${c.courseName}</option>`).join("");
          scheduleRoom.innerHTML = rooms.map(r => `<option value="${r.roomId}">${r.roomNumber} - ${r.buildingName}</option>`).join("");
        };
        scheduleForm.addEventListener("submit", async e => {
          e.preventDefault(); const id = scheduleId.value;
          const data = { studentId: Number(scheduleStudent.value), courseId: Number(scheduleCourse.value), roomId: Number(scheduleRoom.value), classDate: scheduleDate.value, startingTime: `${scheduleStart.value}:00`, endingTime: `${scheduleEnd.value}:00` };
          try { await sendStudentRequest(id ? `/schedules/${id}` : "/schedules", id ? "PUT" : "POST", data); scheduleForm.reset(); scheduleId.value = ""; scheduleSubmit.textContent = "Create schedule"; cancelSchedule.hidden = true; message(id ? "Schedule updated successfully." : "Schedule created successfully."); await load(); } catch (e) { message(e.message, true); }
        });
        scheduleRows.addEventListener("click", async e => {
          const edit = e.target.dataset.scheduleEdit, del = e.target.dataset.scheduleDelete;
          if (edit) { const s = schedules.find(x => x.scheduleId === Number(edit)); scheduleId.value = s.scheduleId; scheduleStudent.value = s.studentId; scheduleCourse.value = s.courseId; scheduleRoom.value = s.roomId; scheduleDate.value = s.classDate; scheduleStart.value = s.startingTime.slice(0, 5); scheduleEnd.value = s.endingTime.slice(0, 5); scheduleSubmit.textContent = "Update schedule"; cancelSchedule.hidden = false; }
          else if (del && confirm("Delete this schedule record?")) { try { await sendStudentRequest(`/schedules/${del}`, "DELETE"); message("Schedule deleted successfully."); await load(); } catch (e) { message(e.message, true); } }
        });
        cancelSchedule.onclick = () => { scheduleForm.reset(); scheduleId.value = ""; scheduleSubmit.textContent = "Create schedule"; cancelSchedule.hidden = true; };
        loadOptions().then(load).catch(e => message(e.message, true));
      }

      const savedPlaceForm = document.getElementById("savedPlaceForm");
      if (savedPlaceForm) {
        const studentId = localStorage.getItem("studentId");
        const list = document.getElementById("savedPlaceList");
        const message = (text, error = false) => showFormStatus("savedPlaceStatus", text, error);
        if (!studentId) {
          message("Log in as a student to manage saved places.", true);
          list.innerHTML = "";
        } else {
          let places = [];
          const load = async () => {
            places = await sendStudentRequest(`/saved-places/student/${studentId}`, "GET");
            list.innerHTML = places.length ? places.map(place =>
              `<div class="saved-card"><div class="saved-info"><h3>${place.savedName}</h3><p>${place.buildingName}</p></div><button type="button" class="delete-btn" data-saved-delete="${place.savedId}">Remove</button></div>`).join("")
              : "<p>No saved places yet.</p>";
          };
          const loadBuildings = async () => {
            const buildings = await sendStudentRequest("/buildings", "GET");
            savedPlaceBuilding.innerHTML = buildings.map(building =>
              `<option value="${building.buildingId}">${building.buildingName}</option>`).join("");
          };
          savedPlaceForm.addEventListener("submit", async event => {
            event.preventDefault();
            try {
              await sendStudentRequest("/saved-places", "POST", {
                studentId: Number(studentId),
                buildingId: Number(savedPlaceBuilding.value),
                savedName: savedPlaceName.value.trim()
              });
              savedPlaceForm.reset();
              message("Place saved successfully.");
              await load();
            } catch (error) {
              message(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true);
            }
          });
          list.addEventListener("click", async event => {
            const id = event.target.dataset.savedDelete;
            if (id && confirm("Remove this saved place?")) {
              try {
                await sendStudentRequest(`/saved-places/${id}`, "DELETE");
                message("Place removed successfully.");
                await load();
              } catch (error) {
                message(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true);
              }
            }
          });
          loadBuildings().then(load).catch(error => message(error.message, true));
        }
      }
    }
    async function loadRooms() {
      try { rooms = await sendStudentRequest("/rooms", "GET"); renderRooms(); }
      catch (error) { roomMessage(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true); }
    }
    async function loadRoomBuildings() {
      const buildingSelect = document.getElementById("roomBuilding");
      buildingSelect.innerHTML = '<option value="">Loading buildings...</option>';
      buildingSelect.disabled = true;
      try {
        buildingsForRooms = await sendStudentRequest("/buildings", "GET");
        if (!buildingsForRooms.length) {
          buildingSelect.innerHTML = '<option value="">No buildings available</option>';
          roomMessage("No buildings are available. Create a building before adding a room.", true);
          return;
        }
        buildingSelect.innerHTML = '<option value="">Select a building</option>' +
          buildingsForRooms
            .map(building => `<option value="${building.buildingId}">${building.buildingName} (${building.buildingCode})</option>`)
            .join("");
        buildingSelect.disabled = false;
      } catch (error) {
        buildingSelect.innerHTML = '<option value="">Unable to load buildings</option>';
        roomMessage(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true);
      }
    }
    roomForm.addEventListener("submit", async event => {
      event.preventDefault();
      const id = document.getElementById("roomId").value;
      const payload = { roomNumber: document.getElementById("roomNumber").value.trim(),
        floorNumber: Number(document.getElementById("floorNumber").value),
        buildingId: Number(document.getElementById("roomBuilding").value) };
      try {
        await sendStudentRequest(id ? `/rooms/${id}` : "/rooms", id ? "PUT" : "POST", payload);
        roomForm.reset(); document.getElementById("roomId").value = "";
        document.getElementById("roomSubmit").textContent = "Create room"; document.getElementById("cancelRoom").hidden = true;
        roomMessage(id ? "Room updated successfully." : "Room created successfully."); await loadRooms();
      } catch (error) { roomMessage(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true); }
    });
    roomRows.addEventListener("click", async event => {
      const editId = event.target.dataset.roomEdit, deleteId = event.target.dataset.roomDelete;
      if (editId) {
        const room = rooms.find(item => item.roomId === Number(editId));
        document.getElementById("roomId").value = room.roomId; document.getElementById("roomNumber").value = room.roomNumber;
        document.getElementById("floorNumber").value = room.floorNumber; document.getElementById("roomBuilding").value = room.buildingId;
        document.getElementById("roomSubmit").textContent = "Update room"; document.getElementById("cancelRoom").hidden = false;
      } else if (deleteId && confirm("Delete this room record?")) {
        try { await sendStudentRequest(`/rooms/${deleteId}`, "DELETE"); roomMessage("Room deleted successfully."); await loadRooms(); }
        catch (error) { roomMessage(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true); }
      }
    });
    document.getElementById("cancelRoom").addEventListener("click", () => {
      roomForm.reset(); document.getElementById("roomId").value = ""; document.getElementById("roomSubmit").textContent = "Create room"; document.getElementById("cancelRoom").hidden = true;
    });
    roomSearch.addEventListener("input", renderRooms);
    loadRoomBuildings().then(loadRooms).catch(error => roomMessage(error.message, true));
  }
  if (buildingForm) {
    let buildings = [];
    const buildingRows = document.getElementById("buildingRows");
    const buildingSearch = document.getElementById("buildingSearch");
    const buildingMessage = (message, isError = false) =>
      showFormStatus("buildingStatus", message, isError);

  function renderBuildings() {
    const query = buildingSearch.value.trim().toLowerCase();
    buildingRows.innerHTML = buildings
      .filter(building => [building.buildingName, building.buildingCode, building.description]
        .some(value => value.toLowerCase().includes(query)))
      .map(building => `<tr>
        <td>${building.buildingName}</td><td>${building.buildingCode}</td>
        <td>${building.description}</td>
        <td><button type="button" class="table-button" data-building-edit="${building.buildingId}">Edit</button>
        <button type="button" class="table-button danger" data-building-delete="${building.buildingId}">Delete</button></td>
      </tr>`).join("");
  }

  async function loadBuildings() {
    try {
      buildings = await sendStudentRequest("/buildings", "GET");
      renderBuildings();
    } catch (error) {
      buildingMessage(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true);
    }
  }

  buildingForm.addEventListener("submit", async event => {
    event.preventDefault();
    const id = document.getElementById("buildingId").value;
    const payload = {
      buildingName: document.getElementById("buildingName").value.trim(),
      buildingCode: document.getElementById("buildingCode").value.trim(),
      description: document.getElementById("buildingDescription").value.trim()
    };
    try {
      await sendStudentRequest(id ? `/buildings/${id}` : "/buildings", id ? "PUT" : "POST", payload);
      buildingForm.reset();
      document.getElementById("buildingId").value = "";
      document.getElementById("buildingSubmit").textContent = "Create building";
      document.getElementById("cancelBuilding").hidden = true;
      buildingMessage(id ? "Building updated successfully." : "Building created successfully.");
      await loadBuildings();
    } catch (error) {
      buildingMessage(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true);
    }
  });

  buildingRows.addEventListener("click", async event => {
    const editId = event.target.dataset.buildingEdit;
    const deleteId = event.target.dataset.buildingDelete;
    if (editId) {
      const building = buildings.find(item => item.buildingId === Number(editId));
      document.getElementById("buildingId").value = building.buildingId;
      document.getElementById("buildingName").value = building.buildingName;
      document.getElementById("buildingCode").value = building.buildingCode;
      document.getElementById("buildingDescription").value = building.description;
      document.getElementById("buildingSubmit").textContent = "Update building";
      document.getElementById("cancelBuilding").hidden = false;
    } else if (deleteId && confirm("Delete this building record?")) {
      try {
        await sendStudentRequest(`/buildings/${deleteId}`, "DELETE");
        buildingMessage("Building deleted successfully.");
        await loadBuildings();
      } catch (error) {
        buildingMessage(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message, true);
      }
    }
  });

  document.getElementById("cancelBuilding").addEventListener("click", () => {
    buildingForm.reset();
    document.getElementById("buildingId").value = "";
    document.getElementById("buildingSubmit").textContent = "Create building";
    document.getElementById("cancelBuilding").hidden = true;
  });
  buildingSearch.addEventListener("input", renderBuildings);
    loadBuildings();
  }
}

const loginForm = document.getElementById("loginForm");

const standaloneLecturerForm = document.getElementById("lecturerForm");
if (standaloneLecturerForm) {
  if (localStorage.getItem("userRole") !== "ADMIN" || !localStorage.getItem("adminSession")) {
    window.location.href = "index.html";
  }
  const lecturerRows = document.getElementById("lecturerRows");
  const lecturerSearch = document.getElementById("lecturerSearch");
  const lecturerStatus = message => showFormStatus("lecturerStatus", message);
  const lecturerError = message => showFormStatus("lecturerStatus", message, true);
  let standaloneLecturers = [];

  function renderStandaloneLecturers() {
    const query = lecturerSearch.value.trim().toLowerCase();
    lecturerRows.innerHTML = standaloneLecturers
      .filter(lecturer => `${lecturer.firstName} ${lecturer.lastName} ${lecturer.email}`.toLowerCase().includes(query))
      .map(lecturer => `<tr><td>${lecturer.firstName} ${lecturer.lastName}</td><td>${lecturer.email}</td>
        <td><button type="button" class="table-button" data-lecturer-edit="${lecturer.lecturerId}">Edit</button>
        <button type="button" class="table-button danger" data-lecturer-delete="${lecturer.lecturerId}">Delete</button></td></tr>`)
      .join("");
  }

  async function loadStandaloneLecturers() {
    try {
      standaloneLecturers = await sendStudentRequest("/lecturers", "GET");
      renderStandaloneLecturers();
    } catch (error) {
      lecturerError(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message);
    }
  }

  standaloneLecturerForm.addEventListener("submit", async event => {
    event.preventDefault();
    const id = document.getElementById("lecturerId").value;
    const payload = {
      firstName: document.getElementById("lecturerFirst").value.trim(),
      lastName: document.getElementById("lecturerLast").value.trim(),
      email: document.getElementById("lecturerEmail").value.trim(),
      officeId: 0
    };
    try {
      await sendStudentRequest(id ? `/lecturers/${id}` : "/lecturers", id ? "PUT" : "POST", payload);
      standaloneLecturerForm.reset();
      document.getElementById("lecturerId").value = "";
      document.getElementById("lecturerSubmit").textContent = "Create lecturer";
      document.getElementById("cancelLecturer").hidden = true;
      lecturerStatus(id ? "Lecturer updated successfully." : "Lecturer created successfully.");
      await loadStandaloneLecturers();
    } catch (error) {
      lecturerError(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message);
    }
  });

  lecturerRows.addEventListener("click", async event => {
    const editId = event.target.dataset.lecturerEdit;
    const deleteId = event.target.dataset.lecturerDelete;
    if (editId) {
      const lecturer = standaloneLecturers.find(item => item.lecturerId === Number(editId));
      document.getElementById("lecturerId").value = lecturer.lecturerId;
      document.getElementById("lecturerFirst").value = lecturer.firstName;
      document.getElementById("lecturerLast").value = lecturer.lastName;
      document.getElementById("lecturerEmail").value = lecturer.email;
      document.getElementById("lecturerSubmit").textContent = "Update lecturer";
      document.getElementById("cancelLecturer").hidden = false;
    } else if (deleteId && confirm("Delete this lecturer record?")) {
      try {
        await sendStudentRequest(`/lecturers/${deleteId}`, "DELETE");
        lecturerStatus("Lecturer deleted successfully.");
        await loadStandaloneLecturers();
      } catch (error) {
        lecturerError(error instanceof TypeError ? "Cannot connect to the Spring Boot API." : error.message);
      }
    }
  });

  document.getElementById("cancelLecturer").addEventListener("click", () => {
    standaloneLecturerForm.reset();
    document.getElementById("lecturerId").value = "";
    document.getElementById("lecturerSubmit").textContent = "Create lecturer";
    document.getElementById("cancelLecturer").hidden = true;
  });
  lecturerSearch.addEventListener("input", renderStandaloneLecturers);
  loadStandaloneLecturers();
}

if (loginForm) {
  loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();
    showFormStatus("loginStatus", "");

    const email = document.getElementById("studentEmail").value.trim();
    const password = document.getElementById("password").value;

    if (!email || !password) {
      showFormStatus("loginStatus", "Please fill in both fields.", true);
      return;
    }

    const submitButton = loginForm.querySelector("button[type='submit']");
    submitButton.disabled = true;
    submitButton.textContent = "Logging in...";

    try {
      const result = await sendApiRequest("/login", {
        studentEmail: email,
        password
      });

      localStorage.setItem("isLoggedIn", "true");
      localStorage.setItem("userRole", result.role);
      if (result.role === "ADMIN") {
        localStorage.setItem("adminSession", result.adminToken);
        window.location.href = "students.html";
      } else {
        localStorage.removeItem("adminSession");
        localStorage.setItem("studentId", String(result.studentId));
        localStorage.setItem("studentName", result.fullName);
        window.location.href = "home.html";
      }
    } catch (error) {
      showFormStatus(
        "loginStatus",
        error instanceof TypeError
          ? "Cannot connect to the server. Start the Spring Boot API and try again."
          : error.message,
        true
      );
    } finally {
      submitButton.disabled = false;
      submitButton.textContent = "Login";
    }
  });
}

const registerForm = document.getElementById("registerForm");

if (registerForm) {
  registerForm.addEventListener("submit", async function (e) {
    e.preventDefault();
    showFormStatus("registerStatus", "");

    const fullName = document.getElementById("fullName").value.trim();
    const studentNumber = document.getElementById("studentNumber").value.trim();
    const email = document.getElementById("studentEmail").value.trim();
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (!fullName || !studentNumber || !email || !password || !confirmPassword) {
      showFormStatus("registerStatus", "Please fill in all fields.", true);
      return;
    }

    if (password !== confirmPassword) {
      showFormStatus("registerStatus", "Passwords do not match.", true);
      return;
    }

    if (password.length < 6) {
      showFormStatus("registerStatus", "Password must be at least 6 characters.", true);
      return;
    }

    const submitButton = registerForm.querySelector("button[type='submit']");
    submitButton.disabled = true;
    submitButton.textContent = "Creating account...";

    try {
      await sendApiRequest("/register", {
        studentNumber,
        fullName,
        studentEmail: email,
        password
      });

      window.location.href = "index.html?registered=true";
    } catch (error) {
      showFormStatus(
        "registerStatus",
        error instanceof TypeError
          ? "Cannot connect to the server. Start the Spring Boot API and try again."
          : error.message,
        true
      );
    } finally {
      submitButton.disabled = false;
      submitButton.textContent = "Register";
    }
  });
}

function removePlace(button) {
  const card = button.closest(".saved-card");
  card.remove();
  alert("Place removed from saved list");
}

const profileForm = document.getElementById("profileForm");

if (profileForm) {
  profileForm.addEventListener("submit", function (e) {
    e.preventDefault();
    alert("Profile updated successfully!");
  });
}

function resetForm() {
  document.getElementById("profileForm").reset();
}

// ===============================
// CPUT DISTRICT SIX OPENSTREETMAP
// ===============================

let campusMap;

const campusMapElement =
  document.getElementById("campusMap");

if (campusMapElement) {

  // CPUT District Six approximate location
  const campusLatitude = -33.931417;
  const campusLongitude = 18.428664;

  // Create map
  campusMap = L.map("campusMap").setView(
    [campusLatitude, campusLongitude],
    18
  );

  // Add OpenStreetMap tiles
  L.tileLayer(
    "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
    {
      maxZoom: 19,
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a> contributors'
    }
  ).addTo(campusMap);


  // ===============================
  // BUILDING LOCATIONS
  // ===============================

  const buildings = [

    {
      name: "CPUT District Six Library",
      latitude: -33.931500,
      longitude: 18.428500,
      description:
        "The CPUT District Six Library provides study areas, computers, books and academic resources for students."
    },

    {
      name: "Engineering Building",
      latitude: -33.931700,
      longitude: 18.428800,
      description:
        "The Engineering Building contains classrooms, laboratories and facilities for engineering students."
    },

    {
      name: "Student Centre",
      latitude: -33.931300,
      longitude: 18.428900,
      description:
        "The Student Centre provides student services, facilities and areas where students can relax and meet."
    }

  ];


  // ===============================
  // CREATE BUILDING MARKERS
  // ===============================

  buildings.forEach(building => {

    const marker = L.marker([
      building.latitude,
      building.longitude
    ]).addTo(campusMap);

    marker.bindTooltip(building.name);

    marker.on("click", function () {

      const infoCard =
        document.getElementById("infoCard");

      const infoTitle =
        document.getElementById("infoTitle");

      const infoDescription =
        document.getElementById("infoDescription");

      infoTitle.textContent =
        building.name;

      infoDescription.textContent =
        building.description;

      infoCard.classList.remove("hidden");

    });

  });

}

// Close building information
function closeInfo() {
  if (infoCard) {
    infoCard.classList.add("hidden");
  }
}

// Save building
function savePlace() {
  const placeName = infoTitle.textContent;

  alert(placeName + " has been saved to your Saved Places!");
}

function searchLocation() {

  const searchInput =
    document.getElementById("searchInput");

  if (!searchInput) {
    return;
  }

  const query =
    searchInput.value.trim().toLowerCase();

  if (!query) {
    alert("Please enter a building name");
    return;
  }

  const buildings = [

    {
      name: "CPUT District Six Library",
      latitude: -33.931500,
      longitude: 18.428500,
      description:
        "The CPUT District Six Library provides study areas, computers, books and academic resources for students."
    },

    {
      name: "Engineering Building",
      latitude: -33.931700,
      longitude: 18.428800,
      description:
        "The Engineering Building contains classrooms, laboratories and facilities for engineering students."
    },

    {
      name: "Student Centre",
      latitude: -33.931300,
      longitude: 18.428900,
      description:
        "The Student Centre provides student services, facilities and areas where students can relax and meet."
    }

  ];

  const result = buildings.find(building =>
    building.name.toLowerCase().includes(query)
  );

  if (!result) {

    alert("Building not found.");

    return;
  }

  // Move map to building
  campusMap.setView(
    [result.latitude, result.longitude],
    19
  );

  // Show information
  document.getElementById("infoTitle").textContent =
    result.name;

  document.getElementById("infoDescription").textContent =
    result.description;

  document.getElementById("infoCard").classList.remove("hidden");
}

function toggleDarkMode() {
  document.body.classList.toggle("dark");
}

function toggleMenu() {
  const sidebar = document.getElementById("sidebar");
  sidebar.classList.toggle("active");
}

const sidebar = document.getElementById("sidebar");

if (sidebar) {
  document.querySelectorAll(".sidebar-link").forEach(link => {
    link.addEventListener("click", () => {
      sidebar.classList.remove("active");
    });
  });
}

const loginLink = document.getElementById("loginLink");

if (loginLink && localStorage.getItem("isLoggedIn") === "true") {
  loginLink.style.display = "none";
}

function logout() {
  localStorage.removeItem("isLoggedIn");
  localStorage.removeItem("userRole");
  localStorage.removeItem("adminSession");
  window.location.href = "index.html";
}