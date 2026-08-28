const loginForm = document.getElementById("loginForm");

if (loginForm) {
  loginForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const email = document.getElementById("studentEmail").value.trim();
    const password = document.getElementById("password").value;

    if (!email || !password) {
      alert("Please fill in both fields");
      return;
    }

    // Save login status
    localStorage.setItem("isLoggedIn", "true");

    // Redirect to home
    window.location.href = "home.html";
  });
}

const registerForm = document.getElementById("registerForm");

if (registerForm) {
  registerForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const fullName = document.getElementById("fullName").value.trim();
    const studentNumber = document.getElementById("studentNumber").value.trim();
    const email = document.getElementById("studentEmail").value.trim();
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (!fullName || !studentNumber || !email || !password || !confirmPassword) {
      alert("Please fill in all fields");
      return;
    }

    if (password !== confirmPassword) {
      alert("Passwords do not match");
      return;
    }

    if (password.length < 6) {
      alert("Password must be at least 6 characters");
      return;
    }

    alert("Registration successful! You can now login.");

    window.location.href = "index.html";
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
  window.location.href = "index.html";
}