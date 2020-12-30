package br.com.mensageria;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TesteConsumidor {

	public static void main(String[] args) throws JMSException, NamingException {
		
		/*
		 * Quando é inicializado o MOM ele já disponibiliza a conexão para o factory dentro de um registro jndi
		 * o que precisamos fazer é pegar esse factory e inserir na nossa classe, ao instanciar o new InitialContext();
		 * automaticamente ele procura um arquivo chamado jndi.properties que é o que foi criado no classpath
		 */
		InitialContext context = new InitialContext();
		
		/*
		 * O Nome da conexão dentro do lookup é o que o MOM sobe quando iniciado, esse nome precisa ser verificado
		 * na documentação de cada MOM
		 */
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		/*
		 * O Session serve para trabalhar com a transação e configurar como será o recebimento da mensagem
		 * o connection apenas faz as ligações entre a aplicação e o serviço de mensagem
		 * createSession recebe dois booleans o primeiro informa se eu quero ou não uma transação
		 * o segundo é o tipo de confirmação de recebimento da mensagem automaticamente
		 */
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		/*
		 * O Lookup busca a fila.financeiro que criamos no ActiveMQ através do alias criado para ela no arquivo
		 * jndi.properties
		 */
		Destination fila = (Destination) context.lookup("financeiro");
		
		/*
		 * O consumer vai buscar as mensagens para serem consumidas atraves da fila
		 */
		MessageConsumer consumer = session.createConsumer(fila);
		
		/*
		 * O método receive retorna uma mensagem.
		 */
		Message message = consumer.receive();
		
		System.out.println("Recebendo mensagem: " + message);
		
		/*
		 * O Scanner é apenas para manter a conexão aberta
		 */
		new Scanner(System.in).nextLine();		
		
		session.close();
		connection.close();
		context.close();

	}

}
