package Model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class VariableLessExpression implements Valuable {
	
		//Use JS engine for expression evaluation
		private String expressionLitteral;
		private ScriptEngineManager mgr = new ScriptEngineManager();
		private ScriptEngine engine = mgr.getEngineByName("JavaScript");
		
		public VariableLessExpression(String expressionLitteral) {
			super();
			this.expressionLitteral = expressionLitteral;
		}

		public String getExpressionLitteral() {
			return expressionLitteral;
		}

		public void setExpressionLitteral(String expressionLitteral) {
			this.expressionLitteral = expressionLitteral;
		}
		
		@Override
		public Object getValue() {
			try {	
				return engine.eval(expressionLitteral);
			} catch (ScriptException e) {
				
				return null;
				
			}
		}

		
}
