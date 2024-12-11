/**
 *
 */
const alertElement = document.getElementById("liveToast");

setTimeout(() => {
  alertElement.style.display = "none";
}, 4000);

document.addEventListener("DOMContentLoaded", function () {
  // Check for message in session storage (more secure approach)
  let message = sessionStorage.getItem("Loginmessage");

  if (message !== null && message !== "") {
    let toastEl = document.getElementById("liveToast");
    let toastBody = toastEl.querySelector(".toast-body");
    toastBody.textContent = message;

    // Ensure Bootstrap Toast is included
    if (
      typeof bootstrap !== "undefined" &&
      typeof bootstrap.Toast === "function"
    ) {
      let toast = new bootstrap.Toast(toastEl);
      toast.show();
    } else {
      console.error("Bootstrap Toast is not loaded.");
    }

    // Remove message from session storage after display (optional)
    sessionStorage.removeItem("Loginmessage");
  }
});
