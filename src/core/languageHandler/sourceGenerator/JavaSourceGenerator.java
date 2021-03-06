package core.languageHandler.sourceGenerator;

import java.util.logging.Logger;

import staticResources.BootStrapResources;
import utilities.Function;
import core.languageHandler.Language;
import core.scheduler.SchedulingData;

public class JavaSourceGenerator extends AbstractSourceGenerator {

	private static final Logger LOGGER = Logger.getLogger(JavaSourceGenerator.class.getName());

	public JavaSourceGenerator() {
		super();
		this.sourceScheduler.setSleepSource(new Function<Long, String>() {
			@Override
			public String apply(Long r) {
				return FOUR_TAB + "controller.blockingWait(" + r + ");\n";
			}
		});
	}

	@Override
	public boolean internalSubmitTask(long time, String device, String action, int[] param) {
		String mid = "";
		if (device.equals("mouse")) {
			if (action.equals("move")) {
				mid = "controller.mouse().move(" + param[0] + ", " + param[1] +");\n";
			} else if (action.equals("moveBy")) {
				mid = "controller.mouse().moveBy(" + param[0] + ", " + param[1] +");\n";
			} else if (action.equals("click")) {
				mid = "controller.mouse().click(" + param[0] + ");\n";
			} else if (action.equals("press")) {
				mid = "controller.mouse().press(" + param[0] + ");\n";
			} else if (action.equals("release")) {
				mid = "controller.mouse().release(" + param[0] + ");\n";
			} else {
				return false;
			}
		} else if (device.equals("keyBoard")) {
			if (action.equals("type")) {
				mid = "controller.keyBoard().type(" + param[0] + ");\n";
			} else if (action.equals("press")) {
				mid = "controller.keyBoard().press(" + param[0] + ");\n";
			} else if (action.equals("release")) {
				mid = "controller.keyBoard().release(" + param[0] + ");\n";
			} else {
				return false;
			}
		} else if (action.equals("wait")) {
			mid = "controller.blockingWait(" + param[0] + ");\n";
		}

		return sourceScheduler.addTask(new SchedulingData<String>(time, FOUR_TAB + mid));
	}

	@Override
	public String getSource() {
		String mainSource = sourceScheduler.getSource();
		if (mainSource == null) {
			LOGGER.severe("Unable to generate source...");
			mainSource = "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(BootStrapResources.getNativeLanguageTemplate(Language.JAVA));
		sb.append(mainSource);
		return sb.toString();
	}
}
