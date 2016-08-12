
/**
 * 
 * @author jun
 *
 */
public class ParamOptimizer extends BaseOptimizer{

	public ParamOptimizer() {
		super();
	}
	
	public static void main(String[] args) {
		new ParamOptimizer().run(args);
	}
	
	@Override
	protected String getHelp() {
		return Usage.PARAM_OPTIMIZATION;
	}
	
	@Override
	protected String confirmExtraArgs() {
		return "";
	}

	@Override
	protected String checkExtraArgs() { return null; }
	
	@Override
	protected void executeExtra() {}

}
