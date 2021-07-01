package validate;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 文本框限制类
 *
 * @author zjx
 * @since 2021/6/11 11:39 上午
 */
public class TxDocument extends PlainDocument {

    private static final long serialVersionUID = 1L;

    int maxLength;

    public TxDocument(int newMaxLength) {
        super();
        maxLength = newMaxLength;
    }

    @Override
    public void insertString(int offset,String str, AttributeSet a) throws BadLocationException {
        //字数超出范围
        if (getLength() + str.length() > maxLength) {
            return;
        } else{
            super.insertString(offset, str, a);
        }
    }
}
