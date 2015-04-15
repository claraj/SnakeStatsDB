import javax.swing.*;
    import javax.swing.table.AbstractTableModel;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.WindowEvent;
    import java.awt.event.WindowListener;
    import java.sql.ResultSet;
    import java.sql.SQLException;


public class Table extends JFrame implements WindowListener{
private JPanel containsTable;
private JTable table1;
private JButton button1;


protected Table(SnakeDataModel sdm) {
    setContentPane(containsTable);
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addWindowListener(this);
    setVisible(true);

    setSize(400, 200);

    //Create a data model and tell our table to use it
    // SnakeDataModel snakeModel = new SnakeDataModel();
    table1.setModel(sdm);
    //Grid color default is white, change it so we can see it
    table1.setGridColor(Color.BLACK);
    //Also make the columns larger
    table1.getColumnModel().getColumn(0).setWidth(400);
    //Quit application when user closes window, otherwise app keeps running
    //Sometimes this is what you want, sometimes it isn't.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Close button code. It would be a good idea
    //to rename this to quitButton or something more descriptive.
    button1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            //Call main's shutdown
            Main.shutdown();
            System.exit(0);
        }
    });

}

//Method provided by WindowListener interface
//Called when user clicks button to close app
public void windowClosing(WindowEvent e) {
    System.out.println("Window closing");
    Main.shutdown();
}

//A WindowListener is required to provide these methods
//We can choose what they do - in this case, nothing
public void windowClosed(WindowEvent e) {}
public void windowOpened(WindowEvent e) {}
public void windowIconified(WindowEvent e) {}
public void windowDeiconified(WindowEvent e) {}
public void windowActivated(WindowEvent e) {}
public void windowDeactivated(WindowEvent e) {}




}
//AbstractTableModel insists that this class implements
//at least these 3 methods; all needed for it to provide
//data for our table
//This data model displays a numbered list of snakes
class SnakeDataModel extends AbstractTableModel {

    ResultSet resultSet;
    int numberOfRows;
    int numberOfColumns;

    SnakeDataModel(ResultSet rs) {
        this.resultSet = rs;
        //Figure out number of rows in resultset
        try {
            numberOfRows = 0;
            while (resultSet.next()) {
                numberOfRows++;
            }
            resultSet.beforeFirst(); //reset cursor to the start

            //Figure out number of columns
            numberOfColumns = resultSet.getMetaData().getColumnCount();

        } catch (SQLException sqle) {
            System.out.println("Error setting up data model" + sqle);
        }
    }

    @Override
    public int getRowCount() {
        return numberOfRows;
    }

    @Override
    public int getColumnCount() {
        return numberOfColumns;
    }

    @Override
//Fetch value for the cell at (row, col).
//The table will call toString on the object, so it's a good idea
//to return a string or something that implements toString in a useful way
    public Object getValueAt(int row, int col) {

        try {
            //Move to this row in the result set. Rows are numbered 1, 2, 3...
            resultSet.absolute(row + 1);
            //And get the column at this row. Columns numbered 1, 2, 3...
            Object o = resultSet.getObject(col + 1);
            return o.toString();
        } catch (SQLException sqle) {
            //Display the text of the error message in the cell
            return sqle.toString();
        }


    }
}

