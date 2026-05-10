@funcional
Feature: Registro de coleta em ponto de coleta

  Como um usuário do sistema
  Quero registrar uma coleta de material
  Para contribuir com o ponto de coleta respeitando sua capacidade

  Scenario: Coleta realizada com sucesso
    Given existe um ponto de coleta válido com capacidade disponível
    And o ponto aceita o tipo de material informado
    When o usuário registra uma coleta de 10 unidades
    Then a coleta deve ser salva com sucesso
    And a quantidade atual do ponto deve ser atualizada

  Scenario: Ponto de coleta não encontrado
    Given não existe um ponto de coleta com o id informado
    When o usuário tenta registrar uma coleta
    Then o sistema deve retornar o erro "Ponto não encontrado"

  Scenario: Material não suportado pelo ponto
    Given existe um ponto de coleta válido
    And o ponto não aceita o tipo de material informado
    When o usuário tenta registrar uma coleta
    Then o sistema deve retornar o erro "Material não suportado nesse ponto"

  Scenario: Capacidade excedida
    Given existe um ponto de coleta com capacidade quase cheia
    When o usuário tenta registrar uma coleta que ultrapassa o limite
    Then o sistema deve retornar o erro "Capacidade excedida para esse material"

  Scenario: Capacidade atingida gera notificação
    Given existe um ponto de coleta com capacidade exata restante
    When o usuário registra uma coleta que atinge o limite máximo
    Then a coleta deve ser salva com sucesso
    And uma notificação deve ser criada informando que a capacidade foi atingida