angular
  .module("locationApp", [])
  .controller("LocationCtrl", function ($scope, $http) {
    const host = "https://provinces.open-api.vn/api/";

    $scope.getCities = function () {
      $http.get(host + "?depth=1").then(function (response) {
        $scope.cities = response.data;
      });
    };

    $scope.getDistricts = function () {
      if ($scope.selectedCity) {
        $http
          .get(host + "p/" + $scope.selectedCity.code + "?depth=2")
          .then(function (response) {
            $scope.districts = response.data.districts;
            $scope.selectedDistrict = null;
            $scope.wards = [];
          });
      }
    };

    $scope.getWards = function () {
      if ($scope.selectedDistrict) {
        $http
          .get(host + "d/" + $scope.selectedDistrict.code + "?depth=2")
          .then(function (response) {
            $scope.wards = response.data.wards;
            $scope.selectedWard = null;
          });
      }
    };

    $scope.getCities();
  });

//Nút số lượng
let data = 0;
document.getElementById("so").innerHTML = data;
function tru() {
  data = data - 1;
  if (data < 0) {
    data = 0;
  }
  document.getElementById("so").innerHTML = data;
}
function cong() {
  data = data + 1;
  document.getElementById("so").innerHTML = data;
}

// angular
//   .module("myApp", ["ngRoute"])
//   .controller("StudentController", StudentController)
//   .controller("FormController", FormController)
//   .controller("TableController", TableController);
// app.config(function ($routeProvider) {
//   $routeProvider.when("/header", {
//     templateUrl: "layout/header.html",
//   });
// });

// function StudentController($scope, $http) {
//   $http.get("http://localhost:3000/student").then((response) => {
//     $scope.students = response.data;
//     $scope.currentStudent = {}; // Use currentStudent for editing
//     $scope.editMode = false; // Flag for edit mode
//   });
// }

// function FormController($scope) {
//   $scope.saveStudent = function () {
//     if (!$scope.editMode) {
//       $scope.students.push(angular.copy($scope.currentStudent));
//     } else {
//       $scope.students[
//         $scope.students.findIndex((s) => s.id === $scope.currentStudent.id)
//       ] = angular.copy($scope.currentStudent);
//       $scope.editMode = false;
//     }
//     $scope.clearForm();
//   };

//   $scope.cancel = function () {
//     if (!$scope.editMode) {
//       $scope.clearForm();
//     } else {
//       $scope.editStudent($scope.currentStudent.index); // Use pre-saved index for edit
//     }
//   };

//   $scope.clearForm = function () {
//     $scope.currentStudent = {};
//   };
// }

// function TableController($scope) {
//   $scope.editStudent = function (index) {
//     const student = $scope.students[index];
//     student.marks = Number.parseFloat(student.marks);
//     student.birthday = new Date(student.birthday);
//     $scope.currentStudent = angular.copy(student);
//     $scope.editMode = true; // Set edit mode flag
//   };

//   $scope.deleteStudent = function (index) {
//     $scope.students.splice(index, 1);
//     $scope.clearForm();
//   };
// }
