import java.util.ArrayList;
import java.util.Scanner;

// --- ETAPA 3: OOP - HERANÇA E ENCAPSULAMENTO ---
abstract class Pessoa {
    private String nome;
    private String cpf;

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    // Getters e Setters (Encapsulamento)
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}

class Eleitor extends Pessoa {
    private final String celular;
    private final String endereco;
    private final int voto;

    public Eleitor(String nome, String cpf, String celular, String endereco, int voto) {
        super(nome, cpf); // Chama o construtor da classe pai (Pessoa)
        this.celular = celular;
        this.endereco = endereco;
        this.voto = voto;
    }

    // Getters e Setters específicos do Eleitor
    public String getCelular() { return celular; }
    public String getEndereco() { return endereco; }
    public int getVoto() { return voto; }
}

public class UrnaEletronica {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ArrayList<Eleitor> listaEleitores = new ArrayList<>();
            String[] candidatos = {"Ana Silva", "Bruno Oliveira", "Carla Souza"};
            int[] votosContagem = new int[3]; 
            int totalVotos = 0;
            
            // Credenciais Admin
            final String LOGIN_ADMIN = "1234";
            final String SENHA_ADMIN = "1234";
            
            boolean sistemaAtivo = true;
            boolean exibirBoletim = true;

            while (sistemaAtivo) {
                System.out.println("\n===========================================");
                System.out.println("       SISTEMA DE VOTAÇÃO - MENU            ");
                System.out.println("===========================================");
                System.out.println("1 - Novo Eleitor (Votar)");
                System.out.println("2 - Área do Administrador (Login)");
                System.out.println("3 - Sair do Sistema");
                System.out.print("Escolha uma opção: ");
                String opcaoPrincipal = scanner.nextLine();

                if (opcaoPrincipal.equals("1")) {
                    System.out.println("\n--- CADASTRO DO ELEITOR ---");

                    // Validação de Nome
                    String nome;
                    while (true) {
                        System.out.print("Nome Completo: ");
                        nome = scanner.nextLine();
                        if (nome.length() >= 3) break;
                        System.out.println("[Erro] O nome deve ter no mínimo 3 caracteres!");
                    }

                    // Validação de CPF (Com checagem de duplicidade)
                    String cpf;
                    while (true) {
                        System.out.print("CPF: ");
                        cpf = scanner.nextLine();
                        
                        if (cpf.length() != 11) {
                            System.out.println("[Erro] O CPF deve ter exatamente 11 dígitos!");
                            continue;
                        }

                        boolean cpfDuplicado = false;
                        for (Eleitor e : listaEleitores) {
                            if (e.getCpf().equals(cpf)) {
                                cpfDuplicado = true;
                                break;
                            }
                        }

                        if (cpfDuplicado) {
                            System.out.println("[Erro] Este CPF já está cadastrado no sistema!");
                        } else {
                            break;
                        }
                    }

                    // Validação de Celular
                    String celular;
                    while (true) {
                        System.out.print("Celular: ");
                        celular = scanner.nextLine();
                        if (celular.length() == 11) break;
                        System.out.println("[Erro] O celular deve ter exatamente 11 dígitos!");
                    }

                    // Validação de Endereço
                    String endereco;
                    while (true) {
                        System.out.print("Endereço: ");
                        endereco = scanner.nextLine();
                        if (endereco.length() >= 5) break;
                        System.out.println("[Erro] O endereço deve ter no mínimo 5 caracteres!");
                    }

                    // --- VOTAÇÃO ---
                    System.out.println("\n--- CANDIDATOS ---");
                    for (int i = 0; i < candidatos.length; i++) {
                        System.out.println((i + 1) + " - " + candidatos[i]);
                    }
                    System.out.print("Digite o número do seu voto: ");
                    
                    try {
                        int voto = Integer.parseInt(scanner.nextLine());
                        if (voto >= 1 && voto <= 3) {
                            votosContagem[voto - 1]++;
                        }
                        totalVotos++;
                        listaEleitores.add(new Eleitor(nome, cpf, celular, endereco, voto));
                        System.out.println("\n[!] VOTO CONFIRMADO COM SUCESSO.");
                    } catch (NumberFormatException e) {
                        System.out.println("[Erro] Entrada inválida. Voto anulado.");
                        totalVotos++;
                        listaEleitores.add(new Eleitor(nome, cpf, celular, endereco, 0));
                    }

                } else if (opcaoPrincipal.equals("2")) {
                    System.out.println("\n--- LOGIN ADM ---");
                    System.out.print("Usuário: ");
                    String user = scanner.nextLine();
                    System.out.print("Senha: ");
                    String pass = scanner.nextLine();

                    if (user.equals(LOGIN_ADMIN) && pass.equals(SENHA_ADMIN)) {
                        boolean menuAdmin = true;
                        while (menuAdmin) {
                            System.out.println("\n---------- PAINEL ADM ----------");
                            System.out.println("1 - Ver Dados (Auditoria)");
                            System.out.println("2 - Encerrar e Ver Resultados");
                            System.out.println("3 - Logout");
                            System.out.print("Opção: ");
                            String opt = scanner.nextLine();

                            switch (opt) {
                                case "1" -> {
                                    System.out.println("\n--- LISTA DE ELEITORES ---");
                                    if(listaEleitores.isEmpty()) System.out.println("Nenhum eleitor.");
                                    for (Eleitor e : listaEleitores) {
                                        String vStr = (e.getVoto() >= 1 && e.getVoto() <= 3) ? candidatos[e.getVoto()-1] : "Nulo";
                                        System.out.println("Nome: " + e.getNome() + " | CPF: " + e.getCpf());
                                        System.out.println("Rua: " + e.getEndereco() + " | Cel: " + e.getCelular());
                                        System.out.println("Voto: " + vStr + "\n--------------------");
                                    }
                                }
                                case "2" -> {
                                    sistemaAtivo = false;
                                    menuAdmin = false;
                                    exibirBoletim = true;
                                }
                                case "3" -> menuAdmin = false;
                                default -> {
                                }
                            }
                        }
                    } else {
                        System.out.println("[Erro] Login ou senha incorretos!");
                    }
                } else if (opcaoPrincipal.equals("3")) {
                    // <--- Nova validação de segurança ao tentar sair pela tela inicial
                    System.out.println("\n--- CONFIRMAÇÃO DE SEGURANÇA (ADM) ---");
                    System.out.print("Usuário Admin: ");
                    String userSair = scanner.nextLine();
                    System.out.print("Senha Admin: ");
                    String passSair = scanner.nextLine();

                    if (userSair.equals(LOGIN_ADMIN) && passSair.equals(SENHA_ADMIN)) {
                        System.out.println("\nSistema encerrado...");
                        sistemaAtivo = false;
                        exibirBoletim = false; // Mantém a regra de ocultar o boletim ao sair por aqui
                    } else {
                        System.out.println("[Erro] Credenciais incorretas! Apenas administradores podem fechar o sistema.");
                    }
                } else {
                    System.out.println("[Erro] Opção inválida! Tente novamente.");
                }
            }

            // --- RESULTADO FINAL ---
            if (exibirBoletim) {
                System.out.println("\n\n===========================================");
                System.out.println("         BOLETIM DE URNA FINAL             ");
                System.out.println("===========================================");
                for (int i = 0; i < candidatos.length; i++) {
                    double porc = (totalVotos > 0) ? ((double) votosContagem[i] / totalVotos) * 100 : 0;
                    System.out.printf("%s: %d votos (%.2f%%)\n", candidatos[i], votosContagem[i], porc);
                }
                System.out.println("Total Geral de Votos: " + totalVotos);
                System.out.println("===========================================");
            }
        }
    }
}