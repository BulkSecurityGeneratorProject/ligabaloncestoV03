'use strict';

angular.module('ligabaloncestoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('equipo', {
                parent: 'entity',
                url: '/equipos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ligabaloncestoApp.equipo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/equipo/equipos.html',
                        controller: 'EquipoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('equipo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('equipo.detail', {
                parent: 'entity',
                url: '/equipo/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ligabaloncestoApp.equipo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/equipo/equipo-detail.html',
                        controller: 'EquipoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('equipo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Equipo', function($stateParams, Equipo) {
                        return Equipo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('equipo.new', {
                parent: 'equipo',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/equipo/equipo-dialog.html',
                        controller: 'EquipoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombreEquipo: null,
                                    localidad: null,
                                    fechaCreacion: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('equipo', null, { reload: true });
                    }, function() {
                        $state.go('equipo');
                    })
                }]
            })
            .state('equipo.edit', {
                parent: 'equipo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/equipo/equipo-dialog.html',
                        controller: 'EquipoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Equipo', function(Equipo) {
                                return Equipo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('equipo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('equipo.delete', {
                parent: 'equipo',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/equipo/equipo-delete-dialog.html',
                        controller: 'EquipoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Equipo', function(Equipo) {
                                return Equipo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('equipo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
