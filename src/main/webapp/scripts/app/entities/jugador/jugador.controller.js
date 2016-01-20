'use strict';

angular.module('ligabaloncestoApp')
    .controller('JugadorController', function ($scope, $state, Jugador, ParseLinks) {

        $scope.jugadors = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Jugador.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.jugadors = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.jugador = {
                nombreJugador: null,
                apellidosJugador: null,
                fechaNacimiento: null,
                numeroTotalcanastas: null,
                numeroTotalasistencias: null,
                numeroTotalrebotes: null,
                posicionCampo: null,
                id: null
            };
        };
    });
