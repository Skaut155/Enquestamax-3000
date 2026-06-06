import Domain.Transactions.DomainController;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ConsoleDriver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DomainController dc = new DomainController();
        boolean exit = false;
        Integer enquestaEnEdicio = null;
        boolean sessioIniciada = false;
        String usuariActual = null;

        while (!exit) {
            if (!sessioIniciada) {
                // Menú d'autenticació
                System.out.println("\n--- AUTENTICACIÓ ---");
                System.out.println("1. Registrar administrador");
                System.out.println("2. Iniciar sessió");
                System.out.println("0. Sortir");
                System.out.print("Escull una opció: ");
                String input = scanner.nextLine();
                int opcio;
                try {
                    opcio = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Opció no vàlida.");
                    continue;
                }
                switch (opcio) {
                    case 1:
                        System.out.print("Nom de l'administrador: ");
                        String nomAdm = scanner.nextLine();
                        System.out.print("Contrasenya: ");
                        String contrasenya = scanner.nextLine();
                        try {
                            dc.registrarAdmin(nomAdm, contrasenya);
                            sessioIniciada = true;
                            usuariActual = nomAdm;
                            System.out.println("Administrador registrat correctament.");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.print("Nom d'usuari: ");
                        String nomUsuari = scanner.nextLine();
                        System.out.print("Contrasenya: ");
                        String pass = scanner.nextLine();
                        try {
                            boolean ok = dc.iniciarSessio(nomUsuari, pass);
                            if (ok) {
                                sessioIniciada = true;
                                usuariActual = nomUsuari;
                                System.out.println("Sessió iniciada correctament.");
                            } else {
                                System.out.println("Usuari o contrasenya incorrectes.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 0:
                        exit = true;
                        System.out.println("Sortint...");
                        break;
                    default:
                        System.out.println("Opció no vàlida.");
                }
                continue;
            }

            if (enquestaEnEdicio == null) {
                // Estat principal: sessió iniciada i sense enquesta seleccionada
                System.out.println("\n--- MENÚ PRINCIPAL --- (Usuari: " + usuariActual + ")");
                System.out.println("1. Crear enquesta");
                System.out.println("2. Eliminar enquesta");
                System.out.println("3. Mostrar llistat d'enquestes");
                System.out.println("4. Iniciar Enquesta (Respondre)");
                System.out.println("5. Editar enquesta");
                System.out.println("6. Clonar enquesta");
                System.out.println("7. Mostrar llistat de respostes");
                System.out.println("8. Mostrar resposta");
                System.out.println("9. Executar clustering");
                System.out.println("10. Executar clustering amb k òptim");
                System.out.println("11. Tancar sessió");
                System.out.println("0. Sortir");
                System.out.print("Escull una opció: ");

                String input = scanner.nextLine();
                int opcio;
                try {
                    opcio = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Opció no vàlida.");
                    continue;
                }

                try {
                    switch (opcio) {
                        case 1:
                            System.out.print("Nom de l'enquesta: ");
                            String nomEnquesta = scanner.nextLine();
                            int id = dc.crearEnquesta(nomEnquesta);
                            System.out.println("Enquesta creada amb ID: " + id);
                            enquestaEnEdicio = id;
                            break;
                        case 2:
                            System.out.print("ID de l'enquesta a eliminar: ");
                            int idElim = Integer.parseInt(scanner.nextLine());
                            dc.eiminarEnquesta(usuariActual,idElim);
                            System.out.println("Enquesta eliminada.");
                            break;
                        case 3:
                            String llistat = dc.getLlistatEnquestes(usuariActual);
                            System.out.println("\n--- LLISTAT D'ENQUESTES ---");
                            System.out.println(llistat);
                            break;
                        case 4:
                            // Nova opció: Iniciar Enquesta (Respondre)
                            System.out.print("ID de l'enquesta a respondre: ");
                            int idEnquestaRespondre = Integer.parseInt(scanner.nextLine());

                            boolean continuarResponent = true;
                            while (continuarResponent) {
                                // Obtenir les preguntes de l'enquesta
                                String[] preguntes = dc.getPreguntesEnquesta(idEnquestaRespondre);
                                List<String> respostes = new ArrayList<>();

                                System.out.println("\n--- RESPONDRE ENQUESTA ---");
                                for (int i = 0; i < preguntes.length; i++) {
                                    System.out.println(preguntes[i]);
                                    System.out.print("Resposta: ");
                                    String resposta = scanner.nextLine();
                                    respostes.add(resposta);
                                }

                                // Guardar totes les respostes de cop

                                try{
                                    dc.guardarRespostes(idEnquestaRespondre, respostes.toArray(new String[0]));
                                    System.out.println("\nRespostes guardades correctament!");
                                }
                                catch (Exception e) {
                                    System.out.println("Error guardant respostes: " + e.getMessage());
                                }
                                finally {
                                    System.out.print("\nVols tornar a respondre aquesta enquesta? (s/n): ");
                                    String continuar = scanner.nextLine();
                                    if (!continuar.equalsIgnoreCase("s")) {
                                        continuarResponent = false;
                                    }
                                }
                            }
                            break;
                        case 5:
                            System.out.print("ID de l'enquesta a editar: ");
                            int idEdit = Integer.parseInt(scanner.nextLine());
                            enquestaEnEdicio = idEdit;
                            System.out.println("Editant enquesta amb ID: " + idEdit);
                            break;
                        case 6:
                            System.out.print("ID de l'enquesta a clonar: ");
                            int idClonar = Integer.parseInt(scanner.nextLine());
                            dc.clonarEnquesta(idClonar, usuariActual);
                            System.out.println("Enquesta clonada.");
                            break;
                        case 7:
                            System.out.print("ID de l'enquesta: ");
                            int idEnquestaRespostes = Integer.parseInt(scanner.nextLine());
                            int[] idsRespostes = dc.getIdsRespostes(idEnquestaRespostes);
                            System.out.println("\n--- LLISTAT DE RESPOSTES ---");
                            if (idsRespostes.length == 0) {
                                System.out.println("No hi ha respostes per aquesta enquesta.");
                            } else {
                                System.out.println("IDs de respostes: ");
                                for (int idResp : idsRespostes) {
                                    System.out.println("- " + idResp);
                                }
                            }
                            break;
                        case 8:
                            System.out.print("ID de l'enquesta: ");
                            int idEnq = Integer.parseInt(scanner.nextLine());
                            System.out.print("ID de la resposta: ");
                            int idResp = Integer.parseInt(scanner.nextLine());
                            String respostaText = dc.consultarResposta(idEnq, idResp);
                            System.out.println("\n--- RESPOSTA ---");
                            System.out.println(respostaText);
                            break;
                        case 9:
                            System.out.print("ID de l'enquesta: ");
                            int idEnqClustering = Integer.parseInt(scanner.nextLine());
                            System.out.print("Nombre de clusters (k): ");
                            int k = Integer.parseInt(scanner.nextLine());
                            System.out.print("Algoritme (K_MEANS_RANDOM, K_MEANS_PLUS_PLUS, K_MEDOIDS): ");
                            String algoritme = scanner.nextLine();

                            int[][] clusters = dc.computeClusterAlgorithm(idEnqClustering, algoritme, k);

                            System.out.println("\n" + formatClusterSizesBarPlot(clusters));
                            System.out.println("\n" + formatClustersAsTable(clusters));
                            break;
                        case 10:
                            System.out.print("ID de l'enquesta: ");
                            int idEnqClusteringOptimal = Integer.parseInt(scanner.nextLine());
                            System.out.print("Threshold ratio (recomanat 0.9): ");
                            double threshold = Double.parseDouble(scanner.nextLine());
                            System.out.print("Algoritme (K_MEANS_RANDOM, K_MEANS_PLUS_PLUS, K_MEDOIDS): ");
                            String algoritmeOptimal = scanner.nextLine();

                            int[][] clustersOptimal = dc.computeClusterAlgorithmOptimalK(idEnqClusteringOptimal, algoritmeOptimal, threshold);

                            System.out.println("\nK òptim trobat: " + clustersOptimal.length);
                            System.out.println("\n" + formatClusterSizesBarPlot(clustersOptimal));
                            System.out.println("\n" + formatClustersAsTable(clustersOptimal));
                            break;
                        case 11:
                            sessioIniciada = false;
                            usuariActual = null;
                            System.out.println("Sessió tancada.");
                            break;
                        case 0:
                            exit = true;
                            System.out.println("Sortint...");
                            break;
                        default:
                            System.out.println("Opció no vàlida.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                // Estat d'edició d'enquesta
                // Mostra l'enquesta abans del menú d'edició
                try {
                    String enquestaText = dc.mostrarEnquesta(enquestaEnEdicio);
                    System.out.println("\n--- ENQUESTA ACTUAL ---");
                    System.out.println(enquestaText);
                } catch (Exception e) {
                    System.out.println("\n(No s'ha pogut mostrar l'enquesta: " + e.getMessage() + ")");
                    enquestaEnEdicio = null;
                    continue;
                }
                System.out.println("\n--- EDICIÓ ENQUESTA (ID: " + enquestaEnEdicio + ") ---");
                System.out.println("1. Afegir pregunta");
                System.out.println("2. Eliminar pregunta");
                System.out.println("3. Canviar tipus de pregunta");
                System.out.println("4. Modificar text de pregunta");
                System.out.println("5. Afegir opció de resposta a una pregunta");
                System.out.println("6. Modificar opció de resposta");
                System.out.println("7. Eliminar opció de resposta");
                System.out.println("8. Canviar obligatorietat de pregunta");
                System.out.println("9. Guardar canvis i sortir de l'edició");
                System.out.println("0. Cancelar canvis i sortir de l'edició");
                System.out.print("Escull una opció: ");

                String input = scanner.nextLine();
                int opcio;
                try {
                    opcio = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Opció no vàlida.");
                    continue;
                }

                try {
                    switch (opcio) {
                        case 1:
                            dc.afegirPregunta(enquestaEnEdicio);
                            System.out.println("Pregunta afegida.");
                            break;
                        case 2:
                            System.out.print("Ordre de la pregunta a eliminar: ");
                            int ordrePreg = Integer.parseInt(scanner.nextLine()) - 1;
                            dc.eliminarPregunta(enquestaEnEdicio, ordrePreg);
                            System.out.println("Pregunta eliminada.");
                            break;
                        case 3:
                            System.out.print("Ordre de la pregunta: ");
                            int ordreTipus = Integer.parseInt(scanner.nextLine()) - 1;
                            System.out.println("Tipus disponibles: NUMERICA, OBERTA, MULTI, UNICA_NO_ORDENADA, UNICA_ORDENADA");
                            System.out.print("Nou tipus: ");
                            String nouTipus = scanner.nextLine();
                            dc.canviarTipusPregunta(enquestaEnEdicio, ordreTipus, nouTipus);
                            System.out.println("Tipus de pregunta canviat.");
                            break;
                        case 4:
                            System.out.print("Ordre de la pregunta: ");
                            int ordreMod = Integer.parseInt(scanner.nextLine()) - 1;
                            System.out.print("Nou text: ");
                            String nouText = scanner.nextLine();
                            dc.modificarPregunta(enquestaEnEdicio, ordreMod, nouText);
                            System.out.println("Text de la pregunta modificat.");
                            break;
                        case 5:
                            System.out.print("Ordre de la pregunta: ");
                            int ordreOpcio = Integer.parseInt(scanner.nextLine()) - 1;
                            System.out.print("Text de la nova opció: ");
                            String textOpcio = scanner.nextLine();
                            dc.afegirOpcioResposta(enquestaEnEdicio, ordreOpcio, textOpcio);
                            System.out.println("Opció de resposta afegida.");
                            break;
                        case 6:
                            System.out.print("Ordre de la pregunta: ");
                            int ordrePregMod = Integer.parseInt(scanner.nextLine()) - 1;
                            System.out.print("Ordre de l'opció: ");
                            int ordreOpcioMod = Integer.parseInt(scanner.nextLine()) - 1;
                            System.out.print("Nou text de l'opció: ");
                            String nouTextOpcio = scanner.nextLine();
                            dc.modificarOpcioResposta(enquestaEnEdicio, ordrePregMod, ordreOpcioMod, nouTextOpcio);
                            System.out.println("Opció de resposta modificada.");
                            break;
                        case 7:
                            System.out.print("Ordre de la pregunta: ") ;
                            int ordrePregElim = Integer.parseInt(scanner.nextLine()) - 1;
                            System.out.print("Ordre de l'opció a eliminar: ");
                            int ordreOpcioElim = Integer.parseInt(scanner.nextLine()) - 1;
                            dc.eliminarOpcioResposta(enquestaEnEdicio, ordrePregElim, ordreOpcioElim);
                            System.out.println("Opció de resposta eliminada.");
                            break;
                        case 8:
                            System.out.print("Ordre de la pregunta: ");
                            int ordreObl = Integer.parseInt(scanner.nextLine()) - 1;
                            System.out.print("Obligatòria? (true/false): ");
                            boolean obligatoria = Boolean.parseBoolean(scanner.nextLine());
                            dc.canviarObligatorietatPregunta(enquestaEnEdicio, ordreObl, obligatoria);
                            System.out.println("Obligatorietat de la pregunta canviada.");
                            break;
                        case 9:
                            dc.guardarEdicioEnquesta(enquestaEnEdicio, usuariActual);
                            System.out.println("Canvis guardats. Sortint de l'edició d'enquesta.");
                            enquestaEnEdicio = null;
                            break;
                        case 0:
                            dc.cancelarEdicioEnquesta(enquestaEnEdicio);
                            enquestaEnEdicio = null;
                            System.out.println("Sortint de l'edició d'enquesta.");
                            break;
                        default:
                            System.out.println("Opció no vàlida.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
        scanner.close();
    }

    /**
     * Formats a bar plot showing the number of responses in each cluster
     * @param clusters array of clusters, each containing response IDs
     * @return formatted bar plot string
     */
    private static String formatClusterSizesBarPlot(int[][] clusters) {
        StringBuilder barPlot = new StringBuilder();

        // Find the maximum cluster size for scaling
        int maxSize = 0;
        for (int[] cluster : clusters) {
            if (cluster.length > maxSize) {
                maxSize = cluster.length;
            }
        }

        // Determine the scale for the bar (max 50 characters)
        int maxBarLength = 50;
        double scale = maxSize > 0 ? (double) maxBarLength / maxSize : 1.0;

        barPlot.append("Cluster Sizes:\n");
        barPlot.append("═".repeat(60)).append("\n");

        for (int i = 0; i < clusters.length; i++) {
            int size = clusters[i].length;
            int barLength = (int) (size * scale);

            // Format: "Cluster 0 (5): ████████ "
            barPlot.append(String.format("Cluster %-2d (%3d): ", i, size));
            barPlot.append("█".repeat(barLength));
            barPlot.append("\n");
        }

        barPlot.append("═".repeat(60)).append("\n");

        return barPlot.toString();
    }

    /**
     * Formats the clustering result as a table string where each column represents a cluster
     * @param clusters array of clusters, each containing response IDs
     * @return formatted table string
     */
    private static String formatClustersAsTable(int[][] clusters) {
        StringBuilder table = new StringBuilder();

        // Find the maximum cluster size to determine number of rows
        int maxSize = 0;
        for (int[] cluster : clusters) {
            if (cluster.length > maxSize) {
                maxSize = cluster.length;
            }
        }

        // Determine column width (enough for "Cluster X" and IDs)
        int colWidth = 12;

        // Create header row
        for (int i = 0; i < clusters.length; i++) {
            table.append(String.format("%-" + colWidth + "s", "Cluster " + i));
            if (i < clusters.length - 1) table.append("| ");
        }
        table.append("\n");

        // Create separator row
        for (int i = 0; i < clusters.length; i++) {
            table.append("-".repeat(colWidth));
            if (i < clusters.length - 1) table.append("+-");
        }
        table.append("\n");

        // Create data rows
        for (int row = 0; row < maxSize; row++) {
            for (int col = 0; col < clusters.length; col++) {
                String cellValue = "";
                if (row < clusters[col].length) {
                    cellValue = "ID: " + clusters[col][row];
                }
                table.append(String.format("%-" + colWidth + "s", cellValue));
                if (col < clusters.length - 1) table.append("| ");
            }
            table.append("\n");
        }

        return table.toString();
    }
}
