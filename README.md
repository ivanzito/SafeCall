# 🛡️ SafeCall

> uma biblioteca leve e extensível para Java e Spring Boot, projetada para fornecer padrões de resiliência e controle de tráfego para APIs de forma simples e eficiente.

📖 Visão Geral (Overview)
O objetivo principal do SafeCall é proteger microserviços contra sobrecarga e falhas em cascata. Diferente de soluções mais robustas e complexas, o SafeCall foca na transparência: uma implementação enxuta que utiliza o poder do Spring AOP (Aspect Oriented Programming) para injetar resiliência nos métodos da aplicação com o mínimo de configuração.

🚀 Funcionalidades (Features)
1. Rate Limiting (Controle de Vazão)
   O SafeCall oferece mecanismos para limitar a quantidade de requisições que um recurso pode processar em um determinado intervalo de tempo, evitando abusos e garantindo a estabilidade do sistema.

Fixed Window (Janela Fixa): Implementação eficiente de baixo overhead para limites rígidos de tempo. Ideal para cenários de cotas simples (ex: 100 requisições por minuto).

Sliding Window Log (Janela Deslizante): Controle de alta precisão que elimina o "efeito de borda" das janelas fixas. Utiliza um rastreamento de timestamps em tempo real para permitir um fluxo de tráfego mais suave e justo.

Extensibilidade de Estratégias: Através da propriedade type na anotação, é possível plugar novas estratégias de execução (como Token Bucket) sem alterar a lógica de negócio.

2. Retry Mechanism (Mecanismo de Retentativa)
   Permite que operações que falharam devido a problemas transitórios (como instabilidade de rede) sejam tentadas novamente de forma automática.

Customização de Exceções: Configure quais erros devem disparar uma nova tentativa.

Estratégias de Intervalo: Controle o tempo de espera entre cada tentativa para evitar sobrecarregar o serviço de destino durante uma falha.

3. Integração Transparente via Anotações
   A utilização da biblioteca é feita de forma declarativa, sem poluir o código fonte com lógicas de controle:


``` Java
    @RateLimit(permits = 50, duration = 60000, type = SlidingWindowExecutor.class)
    public Response processarPagamento(Transaction tx) {
        return service.execute(tx);
    }