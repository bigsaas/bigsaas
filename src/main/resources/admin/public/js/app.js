var bigsaas = angular.module('bigsaas', ['ngResource', 'localization']);

var exampleSocket = new WebSocket("ws://localhost:6080/v1/websocket/", "nodes");

bigsaas.factory('socket', ['$rootScope', function ($rootScope) {
    var socket = exampleSocket
    return {
      on: function (eventName, callback) {
        socket.on(eventName, function () {  
          var args = arguments;
          $rootScope.$apply(function () {
            callback.apply(socket, args);
          });
        });
      },
      emit: function (eventName, data, callback) {
        socket.emit(eventName, data, function () {
          var args = arguments;
          $rootScope.$apply(function () {
            if (callback) {
              callback.apply(socket, args);
            }
          });
        })
      }
    };
  }]);

bigsaas.controller('Controller', ['$scope', 'socket', function ($scope, socket) {

    socket.emit('register')

    socket.on('register', function (data) {
        $scope.data = data;
    });

}]);