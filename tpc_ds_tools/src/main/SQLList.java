/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import bib.base.Base;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael
 */
public class SQLList extends Base {

    public void readFilesSQL() {
        File folder = new File("sql");
        if (folder.exists()) {
            FileOutputStream out = null;
            try {
                System.out.println("Reading sql files.");
                File[] listOfFiles = folder.listFiles();
                String files = "";
                for (int i = 0; i < listOfFiles.length; i++) {
                    File file = listOfFiles[i];
                    if (file.isFile() && file.getName().endsWith(".tpl")) {
                        files += file.getName() + "\n";
                        System.out.println(file.getName());
                    }
                }
                byte dataToWrite[] = files.getBytes();
                out = new FileOutputStream("subqueries_less.lst");
                out.write(dataToWrite);
                out.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SQLList.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SQLList.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(SQLList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void splitQuery() {
        List<String> lista;
        lista = new ArrayList<>();
        File folder = new File("queries");
        File afile[] = folder.listFiles();
        int i = 0;
        int p = 0;
        String nameFile = "";
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            String nome = arquivos.getName();
            String extensao = nome.substring(nome.length() - 4);
            try {
                switch (extensao) {
                    case ".sql":
                        FileReader arq = new FileReader("queries/" + nome);
                        BufferedReader lerArq = new BufferedReader(arq);
                        String linha = "";
                        String texto = "";
                        while (linha != null) {
                            linha = lerArq.readLine();
                            if (linha != null) {
                                if (!linha.contains("--")) {
                                    texto += "\n" + linha;
                                } else {
                                    if (nameFile.isEmpty()) {
                                        nameFile = "/* TPC-DS " + linha + " */ ";
                                    }
                                }
                            }
                            if (linha != null) {
                                if (linha.contains("-- end query")) {
                                    this.writeFile(nameFile + texto, "output/query_" + (p++) + ".sql");
                                    texto = "";
                                    nameFile = "";
                                }
                            }
                        }
                        lista.add(texto);
                        arq.close();
                        break;
                }
            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }
        }
    }

    private void writeFile(String texto, String name) {
        try {
            byte dataToWrite[] = texto.getBytes();
            FileOutputStream out = new FileOutputStream(name);
            out.write(dataToWrite);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SQLList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SQLList.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
