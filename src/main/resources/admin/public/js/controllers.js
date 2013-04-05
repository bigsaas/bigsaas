function NodesCtrl($scope) {
	  $scope.nodes = [{name : "First Node"}, {name : "Second Node"}];
}
NodesCtrl.$inject = ["$scope"];