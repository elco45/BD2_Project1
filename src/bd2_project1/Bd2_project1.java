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
        printPrimeraForma();
        SegundaFN();
        printSegundaForma();
        TerceraFN();
        printTercerForma();
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

    public static void printPrimeraForma() {
        String resp = candidatas.get(0);
        for (int i = 0; i < variables.size(); i++) {
            if (!resp.contains(variables.get(i))) {
                resp += variables.get(i);
            }
        }
        System.out.println("Primera Forma Normal");
        System.out.println(resp);
    }

    public static void printSegundaForma() {
        System.out.println("Segunda Forma Normal");
        for (int i = 0; i < SFN.size(); i++) {
            System.out.println(SFN.get(i));
        }
    }

    public static void printTercerForma() {
        System.out.println("Tercera Forma Normal");
        for (int i = 0; i < TFN.size(); i++) {
            System.out.println(TFN.get(i));
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
        String tmp = "", tmp2, tmp3, combo = "";
        for (int i = 0; i < dependencias.size(); i++) {
            tmp2 = dependencias.get(i).getLadoIzquierdo().replaceAll(",", "");
            tmp3 = dependencias.get(i).getLadoDerecho().replaceAll(" ", "");
            for (int j = 0; j < tmp2.length(); j++) {
                if (!tmp.contains(tmp2.charAt(j) + "")) {
                    tmp += tmp2.charAt(j);
                    combo += tmp2.charAt(j);
                }
            }
            if (!combo.contains(tmp3)) {
                combo += tmp3;
            }
        }
        for (int i = 0; i < variables.size(); i++) {
            if (!combo.contains(variables.get(i))) {
                tmp += variables.get(i);
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
        String tmp = "";
        String primaria = candidatas.get(0);
        ArrayList<dependencia> temporal = new ArrayList();
        for (int i = 0; i < dependencias.size(); i++) {
            temporal.add(new dependencia(dependencias.get(i).getLadoIzquierdo().replaceAll(",", ""), dependencias.get(i).getLadoDerecho().replaceAll(" ", "")));
        }
        for (int i = 0; i < temporal.size(); i++) {//buscar totales
            String tempIz = temporal.get(i).getLadoIzquierdo();
            if (tempIz.length() > 1) {//tiene mas de una variable
                boolean masDeUno = true;
                for (int j = 0; j < tempIz.length(); j++) {
                    if (!primaria.contains(tempIz.charAt(j) + "")) {
                        masDeUno = false;
                    }
                }

                tmp = candidatas.get(0);
                boolean estaSolo = true;
                for (int j = 0; j < temporal.size(); j++) {
                    if (temporal.get(i).getLadoDerecho().equals(temporal.get(j).getLadoDerecho()) && i != j) {
                        estaSolo = false;
                    }
                }
                if (estaSolo) {//derecho
                    if (masDeUno) {//mas de una variable en lado izquierdo
                        for (int j = 0; j < tempIz.length(); j++) {
                            if (!tmp.contains(tempIz.charAt(j) + "")) {
                                tmp += tempIz.charAt(j) + "";
                            }
                        }
                    }
                    boolean tienePrimaria = true;
                    String repetido = "";
                    for (int j = 0; j < tempIz.length(); j++) {
                        if (!primaria.contains(tempIz.charAt(j) + "")) {
                            repetido += tempIz.charAt(j) + "";
                            for (int k = 0; k < temporal.size(); k++) {
                                if (temporal.get(k).getLadoIzquierdo().equals(temporal.get(i).getLadoDerecho()) && !tmp.contains(temporal.get(k).getLadoDerecho())) {
                                    tmp += temporal.get(k).getLadoDerecho();
                                    temporal.remove(k);
                                    k = -1;
                                }
                            }
                        }
                    }
                    for (int j = 0; j < repetido.length(); j++) {
                        if (!tmp.contains(repetido.charAt(j) + "")) {
                            tmp += repetido.charAt(j) + "";
                        }
                    }
                    tmp += temporal.get(i).getLadoDerecho();
                }
            }
        }
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
            if (tmp.length() > 1) {
                SFN.add(tmp);
            }
            tmp = "";
            temporal = new ArrayList();
            for (int x = 0; x < dependencias.size(); x++) {
                temporal.add(new dependencia(dependencias.get(x).getLadoIzquierdo().replaceAll(",", ""), dependencias.get(x).getLadoDerecho().replaceAll(" ", "")));
            }
        }
    }

    public static void TerceraFN() {
        String primaria = candidatas.get(0);
        ArrayList<dependencia> temporal = new ArrayList();
        for (int i = 0; i < dependencias.size(); i++) {
            temporal.add(new dependencia(dependencias.get(i).getLadoIzquierdo().replaceAll(",", ""), dependencias.get(i).getLadoDerecho().replaceAll(" ", "")));
        }
        String relacion1 = primaria;
        String tmp = "";
        boolean contienePrimaria = false;
        for (int i = 0; i < temporal.size(); i++) {
            contienePrimaria = false;
            for (int j = 0; j < temporal.get(i).getLadoIzquierdo().length(); j++) {
                if (primaria.contains(temporal.get(i).getLadoIzquierdo().charAt(j) + "")) {
                    contienePrimaria = true;
                }
            }
            if (contienePrimaria) {
                for (int j = 0; j < temporal.get(i).getLadoIzquierdo().length(); j++) {
                    if (!relacion1.contains(temporal.get(i).getLadoIzquierdo().charAt(j) + "")) {
                        relacion1 += temporal.get(i).getLadoIzquierdo().charAt(j) + "";
                    }
                }
                if (!relacion1.contains(temporal.get(i).getLadoDerecho()) && temporal.get(i).getLadoIzquierdo().length() > 1 && !masDeDos(temporal.get(i).getLadoDerecho())) {
                    relacion1 += temporal.get(i).getLadoDerecho();
                }
                temporal.remove(i);
                i = -1;
            }
        }
        if (relacion1.length()>1) {
            TFN.add(relacion1);
        }
        for (int i = 0; i < temporal.size(); i++) {//los q no llevan partes de la primaria
            contienePrimaria = false;
            for (int j = 0; j < temporal.get(i).getLadoIzquierdo().length(); j++) {
                if (primaria.contains(temporal.get(i).getLadoIzquierdo().charAt(j) + "")) {
                    contienePrimaria = true;
                }
            }
            if (!contienePrimaria) {
                tmp = "";
                for (int j = 0; j < temporal.size(); j++) {
                    if (temporal.get(j).getLadoIzquierdo().equals(temporal.get(i).getLadoIzquierdo())) {//buscar todas las variables de temporal.get(i)
                        if (!tmp.contains(temporal.get(i).getLadoIzquierdo())) {
                            tmp += temporal.get(i).getLadoIzquierdo();
                        }
                        //if ((!tmp.contains(temporal.get(j).getLadoDerecho()) && !masDeDos(temporal.get(j).getLadoDerecho()))) {
                        if (!tmp.contains(temporal.get(j).getLadoDerecho())){
                            tmp += temporal.get(j).getLadoDerecho();
                        }
                    }
                }
                if (!TFN.contains(tmp)) {
                    TFN.add(tmp);
                }
            }
        }
        temporal = new ArrayList();
        for (int i = 0; i < dependencias.size(); i++) {
            temporal.add(new dependencia(dependencias.get(i).getLadoIzquierdo().replaceAll(",", ""), dependencias.get(i).getLadoDerecho().replaceAll(" ", "")));
        }
        for (int i = 0; i < temporal.size(); i++) {
            if (primaria.contains(temporal.get(i).getLadoIzquierdo())) {
                tmp = "";
                for (int j = 0; j < temporal.size(); j++) {
                    if (temporal.get(j).getLadoIzquierdo().equals(temporal.get(i).getLadoIzquierdo())) {//buscar todas las variables de temporal.get(i)
                        if (!tmp.contains(temporal.get(i).getLadoIzquierdo())) {
                            tmp += temporal.get(i).getLadoIzquierdo();
                        }
                        if ((!tmp.contains(temporal.get(j).getLadoDerecho()))) {
                            tmp += temporal.get(j).getLadoDerecho();
                        }
                    }
                }
                if (!TFN.contains(tmp)) {
                    if (!cubreTodaUnaRelacion(TFN.get(0),tmp) && cantPrimaria(tmp)<=1) {
                        TFN.add(tmp);
                    }
                }
            }
        }
    }
    
    public static boolean cubreTodaUnaRelacion(String relacionP,String relacion){
        for (int i = 0; i < relacion.length(); i++) {
            if (!relacionP.contains(relacion.charAt(i)+"")) {
                return false;
            }
        }
        return true;
    }

    public static boolean llevaPrimaria(String carac) {
        boolean esta = false;
        for (int i = 0; i < dependencias.size(); i++) {
            if (dependencias.get(i).getLadoIzquierdo().contains(carac)) {
                for (int j = 0; j < dependencias.get(i).getLadoIzquierdo().length(); j++) {
                    if (candidatas.get(0).contains(dependencias.get(i).getLadoIzquierdo().charAt(j) + "")) {
                        esta = true;
                    }
                }
            }
        }
        return esta;
    }

    public static boolean masDeDos(String carac) {
        int cont = 0;
        for (int i = 0; i < dependencias.size(); i++) {
            if (dependencias.get(i).getLadoDerecho().contains(carac)) {
                cont++;
            }
        }
        if (cont >= 2) {
            return true;
        } else {
            return false;
        }
    }
    
    public static int cantPrimaria(String relacion){
        int cont =0;
        for (int i = 0; i < relacion.length(); i++) {
            if (candidatas.get(0).contains(relacion.charAt(i)+"")) {
                cont++;
            }
        }
        return cont;
    }

}
