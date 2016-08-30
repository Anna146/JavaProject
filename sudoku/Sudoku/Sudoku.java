//import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.runtime.ANTLRInputStream;
//import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
//import org.antlr.v4.runtime.tree.gui.TreeViewer;
import org.antlr.v4.gui.TreeViewer;

public class Sudoku {
	public static void main(String[] args) throws Exception {
        //prepare token stream
		ANTLRInputStream stream = new ANTLRInputStream(System.in);
        GrammarLexer lexer  = new GrammarLexer(stream);   
        TokenStream tokenStream = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokenStream);
        ParseTree tree = parser.formula(); 

        //show AST in console
        System.out.println(tree.toStringTree(parser));

        //show AST in GUI
        JFrame frame = new JFrame("Antlr AST");
        JPanel panel = new JPanel();
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()),tree);
        viewr.setScale(1.5);//scale a little
        panel.add(viewr);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);
        frame.setVisible(true);
    }
}
