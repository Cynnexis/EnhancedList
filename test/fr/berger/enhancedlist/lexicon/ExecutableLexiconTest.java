package fr.berger.enhancedlist.lexicon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

public class ExecutableLexiconTest extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		LexiconTest instance = new LexiconTest();
		Method[] methods = instance.getClass().getDeclaredMethods();
		
		System.out.println("ExecutableLexiconTest> Assessing " + methods.length + " method" + (methods.length > 1 ? "s" : "") + ":");
		
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			
			if (method.getName().equals("setup") || method.getName().equals("tearDown") || method.getName().equals("update") || method.getName().contains("$"))
				continue;
			
			System.out.println(new MessageFormat("\t{0}: {1}()...").format(new Object[] {i+1, method.getName()}));
			
			// Call the setup function
			instance.setup();
			
			// Try to call the method
			try {
				method.invoke(instance);
			} catch (IllegalAccessException ex) {
				System.err.println("ERROR: Cannot access the method.");
				ex.printStackTrace();
			} catch (IllegalArgumentException ex) {
				System.err.println("ERROR: Wrong arguments (" +  method.getParameterCount() + " argument" + (method.getParameterCount() > 1 ? "s are" : " is") + " required)");
				ex.printStackTrace();
			} catch (InvocationTargetException ex) {
				System.err.println("ERROR: Something went wrong while calling the method.");
				ex.printStackTrace();
			} catch (ExceptionInInitializerError ex) {
				System.err.println("ERROR: Something went wrong while initializing the method.");
				ex.printStackTrace();
			}
			
			// Call the tear down function
			instance.tearDown();
		}
		
		Platform.exit();
	}
}
