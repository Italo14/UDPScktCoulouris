/*
 * Servidor.java
 *
 * Created on 17 de Maio de 2006, 11:27
 *
 * Servidor ECHO: fica em aguardo de solicitação de algum cliente. Quando recebe
 * simplesmente devolve a mensagem. Funcionamento: tiro unico
 */

import java.net.*;
import java.io.*;

public class Servidor {
    public static void main(String args[]) {
        DatagramSocket s = null;
        try {
            s = new DatagramSocket(6789); // cria um socket UDP
            byte[] buffer = new byte[1000];
            System.out.println("\n\n*** Servidor aguardando request");
            // cria datagrama para recepcionar solicitação do cliente
            DatagramPacket req = new DatagramPacket(buffer, buffer.length);
            s.receive(req);
            System.out.println("*** Request recebido de: " + req.getAddress()+":"+req.getPort());
            
            // envia resposta
            
            System.out.println("Questões recebidas" + new String(req.getData(), 0, req.getLength()));
            
            
            //////////////////////////Tratamento gabarito///////////////////////////////////////////////////////////
            
            String gabarito = "1;4;VVFF"; 
           
            //dividindo o numero de questoes,numero de alternativas e alternativas//
            
            String[] Gcortada = gabarito.split(";");
            
            //passando os dados dos vetores para as variaveis string //
            
            String G_numero_questoes = Gcortada[0];
            String G_numero_alternativas = Gcortada[1];
            String G_alternativas = Gcortada[2];
            
           /////////////////////////////Tratamento questionario////////////////////////////////////////////////////
            
           //// dados recebidos pelo cliente ////////
            
            String questoes = new String(req.getData(), 0, req.getLength());
            
           //dividindo o numero de questoes,numero de alternativas e alternativas//
            
            String[] Qcortada = questoes.split(";");
            
            //passando os dados dos vetores para as variaveis string //
                           
            String numero_questoes = Qcortada[0];
            String numero_alternativas = Qcortada[1];
            String alternativas = Qcortada[2];
            
            // alimento o vetor char com as alternativas///
            
            char[] g = G_alternativas.toCharArray();
            char[] q = alternativas.toCharArray();
            
            int acertos = 0;
            int erros = 0;
            boolean referencia = false;
            
            ////Laço de comparação e contador de erros e acertos////
            
            if (numero_questoes.equals(G_numero_questoes))
            {
            	referencia = numero_alternativas.equals(G_numero_alternativas);
            	
            	if (referencia == true) {
                	
                	for (int j = 0; j<g.length; j++) {
                		if (q[j] == g[j]) {
                			acertos++;
                		}else 
       	            	 erros++;
	              }
            }
            }
                       
            //////////////////// impresão dos dados numro de questões de acertos e erros/////////////
            
            String correcao = new String("Questão: " + numero_questoes + "; Acerto(s): " + acertos + "; Erro(s): "+ erros + ";");
            System.out.println(correcao);
            
            /////conversão em byte/////
            
            
            byte []by = correcao.getBytes();
            req.setData(by);
            
            
            
            DatagramPacket resp = new DatagramPacket(req.getData(), req.getLength(),
                    req.getAddress(), req.getPort());
            s.send(resp);
            
            
            
        } catch (SocketException e) {
            System.out.println("Erro de socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro envio/recepcao pacote: " + e.getMessage());
        } finally {
            if (s != null) s.close();
        }
    }
}
