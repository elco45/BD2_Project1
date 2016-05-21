package bd2_project1;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author elco45
 */
public class Bd2_project1 {

    static ArrayList<String> variables;
    static ArrayList<dependencia> dependencias;
    static ArrayList<String> superclaves;
    static ArrayList<String> candidatas;
    static ArrayList<String> SFN;
    static ArrayList<String> TFN;

    public static void main(String[] args) {
        variables = new ArrayList();
        dependencias = new ArrayList();
        superclaves = new ArrayList();
        candidatas = new ArrayList();
        SFN = new ArrayList();
        TFN = new ArrayList();
        loadData();
        printVariables();
        printDependencias();
        buscarSuperclaves(getVariables());
        printSuperclaves();
        buscarCandidatas();
        printCandidatas();
        SegundaFN();
        //System.out.println(esSuperclave("ABC"));
    }

    private static boolean loadData() {
        try {
            String line, data;
            BufferedReader reader = new BufferedReader(new java.io.FileReader(new File("./src/Resources/dataText.txt")));
            for (int lineIndex = 0; (line = reader.readLine()) != null; lineIndex++) {
                data = line.replaceAll(" ", "");
                if (lineIndex == 0) {
                    if ((data.charAt(0) == 'r' || data.charAt(0) == 'R') && (data.charAt(1) == '{' && data.charAt(data.length() - 1) == '}')) {
                        System.out.println(data);
                        variables.addAll(Arrays.asList(data.substring(2, data.length() - 1).split(",")));
                    } else {
                        System.err.println("La sintaxis de la relacion no es correcta");
                    }
                } else {
                    if (data.contains("->")) {
                        String[] depTmp = data.split("->");
                        String[] depIz = depTmp[1].split(",");
                        for (int i = 0; i < depIz.length; i++) {
                            dependencias.add(new dependencia(depTmp[0].replaceAll(",", ""), depIz[i].replaceAll(" ", "")));
                        }
                    }
                }
            }
            Collections.sort(variables, String.CASE_INSENSITIVE_ORDER);
            System.out.println("");
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    public static String getVariables() {
        String resp = "";
        for (int i = 0; i < variables.size(); i++) {
            resp += variables.get(i);
        }
        return resp;
    }

    public static void printVariables() {
        System.out.println("Las variables son: ");
        for (int i = 0; i < variables.size(); i++) {
            System.out.print(variables.get(i) + " ");
        }
        System.out.println("");
    }

    public static void printDependencias() {
        System.out.println("Las dependencias son: ");
        for (int i = 0; i < dependencias.size(); i++) {
            System.out.println(dependencias.get(i).getLadoIzquierdo() + " -> " + dependencias.get(i).getLadoDerecho());
        }
    }

    public static void printSuperclaves() {
        System.out.println("Las superclaves son: ");
        for (int i = 0; i < superclaves.size(); i++) {
            System.out.println(superclaves.get(i));
        }
    }

    public static void printCandidatas() {
        System.out.println("Las llaves candidatas son: ");
        for (int i = 0; i < candidatas.size(); i++) {
            System.out.println(candidatas.get(i));
        }
    }

    public static void buscarSuperclaves(String s) {
        combo("", s);
    }

    public static void combo(String prefix, String s) {
        int N = s.length();
        if (soloIz(prefix)) {
            if (esSuperclave(prefix)) {
                if (!superclaves.contains(ordenarString(prefix))) {
                    superclaves.add(prefix);
                }
            }
        }
        for (int i = 0; i < N; i++) {
            combo(prefix + s.charAt(i), s.substring(i + 1));
        }
    }

    public static boolean soloIz(String prefix) {
        String tmp = "", tmp2;
        for (int i = 0; i < dependencias.size(); i++) {
            tmp2 = dependencias.get(i).getLadoIzquierdo().replaceAll(",", "");
            for (int j = 0; j < tmp2.length(); j++) {
                if (!tmp.contains(tmp2.charAt(j) + "")) {
                    tmp += tmp2.charAt(j);
                }
            }
        }
        for (int i = 0; i < prefix.length(); i++) {
            if (!tmp.contains(prefix.charAt(i) + "")) {
                return false;
            }
        }
        return true;
    }

    public static boolean esSuperclave(String perm) {
        String tmp = perm.replaceAll(" ", "");
        String tmpIz = "";
        boolean containsAllIz = true;
        ArrayList<dependencia> temporal = new ArrayList();
        for (int i = 0; i < dependencias.size(); i++) {
            temporal.add(new dependencia(dependencias.get(i).getLadoIzquierdo().replaceAll(" ", ""), dependencias.get(i).getLadoDerecho()));
        }
        for (int i = 0; i < temporal.size(); i++) {
            containsAllIz = true;
            tmpIz = temporal.get(i).getLadoIzquierdo();
            for (int j = 0; j < tmp.length(); j++) {
                for (int k = 0; k < tmpIz.length(); k++) {
                    if (!tmp.contains(tmpIz.charAt(k) + "")) {
                        containsAllIz = false;
                    }
                }

            }
            if (containsAllIz && tmp.contains(temporal.get(i).getLadoDerecho())) {
                temporal.remove(i);
                i = -1;
                continue;
            }
            if (!tmp.contains(temporal.get(i).getLadoDerecho()) && containsAllIz == true) {
                tmp += temporal.get(i).getLadoDerecho();
                temporal.remove(i);
                i = -1;
            }
        }
        if (tmp.length() == variables.size()) {
            return true;
        }
        return false;
    }

    public static void buscarCandidatas() {
        String tmp = "";
        for (int i = 0; i < superclaves.size(); i++) {
            tmp = esCandidata(superclaves.get(i));
            if (!candidatas.contains(tmp)) {
                candidatas.add(tmp);
            }
        }
    }

    public static String esCandidata(String superclave) {
        String tmp = superclave, tmp2 = superclave;
        char borrado = ' ';
        for (int i = 0; i < tmp.length(); i++) {
            borrado = tmp.charAt(i);
            tmp = tmp.replaceAll(tmp.charAt(i) + "", "");

            if (!esSuperclave(tmp)) {
                tmp = tmp2;
            } else {
                tmp2 = tmp;
                i = -1;
            }
        }
        return ordenarString(tmp);
    }

    public static String ordenarString(String str) {
        // put the characters into an array
        Character[] chars = new Character[str.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = str.charAt(i);
        }

        // sort the array
        Arrays.sort(chars, new Comparator<Character>() {
            public int compare(Character c1, Character c2) {
                int cmp = Character.compare(
                        Character.toLowerCase(c1.charValue()),
                        Character.toLowerCase(c2.charValue())
                );
                if (cmp != 0) {
                    return cmp;
                }
                return Character.compare(c1.charValue(), c2.charValue());
            }
        });

        // rebuild the string
        StringBuilder sb = new StringBuilder(chars.length);
        for (char c : chars) {
            sb.append(c);
        }
        str = sb.toString();
        return str;
    }

    public static void SegundaFN() {
        System.out.println("Segunda Forma");
        String tmp = "";
        String primaria = candidatas.get(0);
        ArrayList<dependencia> temporal = new ArrayList();
        for (int i = 0; i < dependencias.size(); i++) {
            temporal.add(new dependencia(dependencias.get(i).getLadoIzquierdo().replaceAll(",", ""), dependencias.get(i).getLadoDerecho().replaceAll(" ", "")));
        }
        for (int i = 0; i < temporal.size(); i++) {//buscar totales
            if (temporal.get(i).getLadoIzquierdo().equals(primaria)) {
                tmp = candidatas.get(0);
                boolean estaSolo = true;
                for (int j = 0; j < temporal.size(); j++) {
                    if (temporal.get(i).getLadoDerecho().equals(temporal.get(j).getLadoDerecho()) && i != j) {
                        estaSolo = false;
                    }
                }
                if (estaSolo) {
                    tmp += temporal.get(i).getLadoDerecho();
                }
            }
        }
        System.out.println(tmp);
        if (tmp.length() > 1) {
            SFN.add(tmp);
        }
        tmp = "";
        String trans = "";
        for (int i = 0; i < primaria.length(); i++) {
            for (int j = 0; j < temporal.size(); j++) {
                if (temporal.get(j).getLadoIzquierdo().equals(primaria.charAt(i) + "")) {
                    trans = "";
                    if (!tmp.contains(primaria.charAt(i) + "")) {
                        tmp = primaria.charAt(i) + "";
                    }
                    if (!tmp.contains(temporal.get(j).getLadoDerecho())) {
                        tmp += temporal.get(j).getLadoDerecho();
                        trans = temporal.get(j).getLadoDerecho();
                        temporal.remove(j);
                        j = -1;
                    }
                    if (trans.length() > 0) {//ver si existe transitividad
                        for (int k = 0; k < tmp.length(); k++) {
                            for (int l = 0; l < temporal.size(); l++) {
                                if (temporal.get(l).getLadoIzquierdo().equals(tmp.charAt(k) + "") && !tmp.contains(temporal.get(l).getLadoDerecho())) {
                                    tmp += temporal.get(l).getLadoDerecho();
                                    temporal.remove(l);
                                    l = -1;
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(tmp);
            tmp = "";
            temporal = new ArrayList();
            for (int x = 0; x < dependencias.size(); x++) {
                temporal.add(new dependencia(dependencias.get(x).getLadoIzquierdo().replaceAll(",", ""), dependencias.get(x).getLadoDerecho().replaceAll(" ", "")));
            }
        }

    }
}
