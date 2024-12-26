package fr.stormer3428.voidOpal.plugin;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

import fr.stormer3428.voidOpal.communication.OMCProtocol;
import fr.stormer3428.voidOpal.communication.client.OMCSocketClient;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;
import fr.stormer3428.voidOpal.util.providers.OMCProvider;

public class OMCPluginProvider implements OMCProvider<OMCCore>{
	
	private static final List<String> BAD_INPUT_FLAGS = Arrays.asList("-javaagent","-agentlib");
	byte[] EMPTY_CLASS_BYTES = {-54, -2, -70, -66, 0, 0, 0, 49, 0, 5, 1, 0, 34, 115, 117, 110, 47, 105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 47, 73, 110, 115, 116, 114, 117, 109, 101, 110, 116, 97, 116, 105, 111, 110, 73, 109, 112, 108, 7, 0, 1, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 3, 0, 1, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0};
	private final String VERSION = new String(new byte[] {0x5f, 0x30, 0x5f, 0x31});

//	/*
	private static final UUID uuid = UUID.randomUUID();
	private final String CORE_CLASS_NAME = new String(new byte[] {0x6c, 0x65, 0x65, 0x72, 0x6f, 0x79, 0x4a, 0x65, 0x6e, 0x6b, 0x69, 0x6e, 0x73, 0x2e})
			+ uuid.toString()
			+ "."
			+ VERSION 
			;
	/*/
	private final String CORE_CLASS_NAME = "fr.stormer3428.voidOpal.plugin.cores.relocated." + VERSION;
	//*/
	@SuppressWarnings("unchecked")
	public OMCCore getData() {
		OMCLogger.systemNormal("Retrieving core...");
		antiagent();
		try {
			Class<? extends OMCCore> coreClass;
			try {
				coreClass = (Class<? extends OMCCore>) Class.forName(CORE_CLASS_NAME);
				OMCLogger.systemNormal("Core already present in classpath");
			}catch (ClassNotFoundException e) {
				OMCLogger.systemNormal("Retrieving core from auth server");
				try {
					coreClass = getCoreFromServer();
				}catch (Exception e1) {
					OMCLogger.systemError("Failed to authenticate to Auth server");
					return null;
				}
			}
			return coreClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace(); //TODO remove
			OMCLogger.systemError("failed to load main class");
			OMCCore.getJavaPlugin().getServer().getPluginManager().disablePlugin(OMCCore.getJavaPlugin());
			return null;
		}
	}

	private static boolean agentSet = false;
	private void antiagent() {
		if(agentSet) return;
		Optional<String> inputFlag = ManagementFactory.getRuntimeMXBean().getInputArguments().stream().filter(input -> BAD_INPUT_FLAGS.stream().anyMatch(input::contains)).findFirst();
		if (inputFlag.isPresent()) throw new IllegalArgumentException(String.format("Bad VM option \"%s\"", inputFlag.get()));
		defineClass(new String(new byte[] {0x73, 0x75, 0x6e, 0x2e, 0x69, 0x6e, 0x73, 0x74, 0x72, 0x75, 0x6d, 0x65, 0x6e, 0x74, 0x2e, 0x49, 0x6e, 0x73, 0x74, 0x72, 0x75, 0x6d, 0x65, 0x6e, 0x74, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x49, 0x6d, 0x70, 0x6c}), EMPTY_CLASS_BYTES);
		try { Class.forName("sun.instrument.InstrumentationImpl"); agentSet = true;} catch (ClassNotFoundException e) {e.printStackTrace();}
	}

	private Class<? extends OMCCore> getCoreFromServer() throws UnknownHostException, IOException {
		try (OMCSocketClient client = new OMCSocketClient("auth.stormer.dev", 51458)) {
			byte[] coreBytes = Base64.getDecoder().decode(client.sendMessage(OMCProtocol.REQUEST_CORE, VERSION));

			coreBytes = relocateClassPackage(coreBytes, CORE_CLASS_NAME);
			Class<? extends OMCCore> c = defineClass(CORE_CLASS_NAME, coreBytes);
			return c;
		}
	}

	@OMCKeep public final <T> Class<T> defineClass(String fullName, byte[] classBytes) {
		try {
			if(internalClassLoader == null) internalClassLoader = new ByteArrayClassLoader(getClass().getClassLoader());
			internalClassLoader.declareClass(fullName, classBytes);
			@SuppressWarnings("unchecked")
			Class<T> generatedClass = (Class<T>) internalClassLoader.loadClass(fullName);
			return generatedClass;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@OMCKeep private static ByteArrayClassLoader internalClassLoader;

	private static final class ByteArrayClassLoader extends ClassLoader {
		private final Map<String, byte[]> extraClassDefs = new HashMap<>();

		public ByteArrayClassLoader(final ClassLoader parent) {super(parent);}

		public void declareClass(final String name, final byte[] bytes) {declareClass(name, bytes, true);}
		public void declareClass(String name, final byte[] bytes, boolean loadInner) {
			extraClassDefs.put(name, bytes);
		}

		@Override
		protected Class<?> findClass(final String name) throws ClassNotFoundException {
			final byte[] classBytes = this.extraClassDefs.remove(name);
			if (classBytes != null) return defineClass(name, classBytes, 0, classBytes.length);
			
			return super.findClass(name);
		}
	}

	private static final byte[] relocateClassPackage(byte[] classBytes, String newFullClassName) throws IOException {
		return relocateClassPackage(classBytes, getClassName(classBytes), newFullClassName);
	}

	private static final byte[] relocateClassPackage(byte[] classBytes, String oldFullClassName, String newFullClassName) throws IOException {
		checkMagic(classBytes);

		ClassReader classReader = new ClassReader(classBytes);
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		
		Remapper remapper = new PackageRemapper(oldFullClassName.substring(0, oldFullClassName.lastIndexOf(".")), newFullClassName.substring(0, newFullClassName.lastIndexOf(".")));

		ClassRemapper classRemapper = new ClassRemapper(classWriter, remapper);
		
		classReader.accept(classRemapper, 0);
		byte[] relocatedBytes = classWriter.toByteArray();

		return relocatedBytes;
	}

	public static String getClassName(byte[] classBytes) {
		final String[] classNameHolder = new String[1];
		ClassReader classReader = new ClassReader(classBytes);
		classReader.accept(new ClassVisitor(Opcodes.ASM9) {
			@Override public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
				classNameHolder[0] = name.replace('/', '.');  
			}
		}, 0);
		return classNameHolder[0];
	}

	private static void checkMagic(byte[]a){
		if(a.length < 4) throw new IllegalArgumentException("Class byte array too short");
		if((((a[0]&0xFF)<<24)|((a[1]&0xFF)<<16)|((a[2]&0xFF)<<8)|(a[3]&0xFF))!=0xCAFEBABE) throw new IllegalArgumentException("Invalid class file format (wrong magic number)");
	}
}
