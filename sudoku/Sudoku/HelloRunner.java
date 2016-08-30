/*import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
public class HelloRunner 
{
    public static void main( String[] args) throws Exception 
    {

        ANTLRInputStream input = new ANTLRInputStream( System.in);

        HelloLexer lexer = new HelloLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        HelloParser parser = new HelloParser(tokens);
        ParseTree tree = parser.r(); // begin parsing at rule 'r'
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
}*/

import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
//import org.antlr.v4.runtime.tree.gui.TreeViewer;
import org.antlr.v4.gui.TreeViewer;

/**
 * A simple demo to show AST GUI with ANTLR
 * @see http://www.antlr.org/api/Java/org/antlr/v4/runtime/tree/gui/TreeViewer.html
 * 
 * @author wangdq
 * 2014-5-24
 *
 */
public class HelloRunner {
    public static void main(String[] args) {
        //prepare token stream
        CharStream stream = new ANTLRInputStream("hello antlr");
        HelloLexer lexer  = new HelloLexer(stream);   
        TokenStream tokenStream = new CommonTokenStream(lexer);
        HelloParser parser = new HelloParser(tokenStream);
        ParseTree tree = parser.r(); 

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
