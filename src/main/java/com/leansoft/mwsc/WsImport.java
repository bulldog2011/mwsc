package com.leansoft.mwsc;

/**
 * WsImport tool entry point.
 * 
 * @author bulldog
 */
public class WsImport {
    /**
     * CLI entry point. Use {@link Invoker} to
     * load tools.jar
     */
    public static void main(String[] args) throws Throwable {
        System.exit(Invoker.invoke("com.leansoft.mwsc.WsimportTool", args));
    }

    /**
     * Entry point for tool integration.
     *
     * <p>
     * This does the same as {@link #main(String[])} except
     * it doesn't invoke {@link System#exit(int)}. This method
     * also doesn't play with classloaders. It's the caller's
     * responsibility to set up the classloader to load all jars
     * needed to run the tool, including <tt>$JAVA_HOME/lib/tools.jar</tt>
     *
     * @return
     *      0 if the tool runs successfully.
     */
    public static int doMain(String[] args) throws Throwable {
        return new WsimportTool(System.out).run(args) ? 0 : 1;
    }
}
