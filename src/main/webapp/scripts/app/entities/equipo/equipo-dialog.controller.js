'use strict';

angular.module('ligabaloncestoApp').controller('EquipoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Equipo', 'Jugador',
        function($scope, $stateParams, $uibModalInstance, entity, Equipo, Jugador) {

        $scope.equipo = entity;
        $scope.jugadors = Jugador.query();
        $scope.load = function(id) {
            Equipo.get({id : id}, function(result) {
                $scope.equipo = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ligabaloncestoApp:equipoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.equipo.id != null) {
                Equipo.update($scope.equipo, onSaveSuccess, onSaveError);
            } else {
                Equipo.save($scope.equipo, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
