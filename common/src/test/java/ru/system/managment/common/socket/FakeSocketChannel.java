package ru.system.managment.common.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * @author Mikhail Zaitsev
 */
public class FakeSocketChannel extends SocketChannel {

  private ByteArrayOutputStream baos = new ByteArrayOutputStream();

  public ByteBuffer getCollectedData(){
    byte[] data = baos.toByteArray();
    ByteBuffer buffer = ByteBuffer.allocate(data.length);
    buffer.put(data);
    return buffer;
  }

  /**
   * Initializes a new instance of this class.
   *
   * @param provider
   */
  public FakeSocketChannel(SelectorProvider provider) {
    super(provider);
  }

  @Override
  public SocketChannel bind(SocketAddress local) throws IOException {
    return null;
  }

  @Override
  public SocketAddress getLocalAddress() throws IOException {
    return null;
  }

  @Override
  public <T> SocketChannel setOption(SocketOption<T> name, T value) throws IOException {
    return null;
  }

  @Override
  public <T> T getOption(SocketOption<T> name) throws IOException {
    return null;
  }

  @Override
  public Set<SocketOption<?>> supportedOptions() {
    return null;
  }

  @Override
  public SocketChannel shutdownInput() throws IOException {
    return null;
  }

  @Override
  public SocketChannel shutdownOutput() throws IOException {
    return null;
  }

  @Override
  public Socket socket() {
    return null;
  }

  @Override
  public boolean isConnected() {
    return false;
  }

  @Override
  public boolean isConnectionPending() {
    return false;
  }

  @Override
  public boolean connect(SocketAddress remote) throws IOException {
    return false;
  }

  @Override
  public boolean finishConnect() throws IOException {
    return false;
  }

  @Override
  public SocketAddress getRemoteAddress() throws IOException {
    return null;
  }

  @Override
  public int read(ByteBuffer dst) throws IOException {
    return 0;
  }

  @Override
  public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
    return 0;
  }

  @Override
  public int write(ByteBuffer src) throws IOException {
    src.rewind();
    while(src.hasRemaining()){
      baos.write(src.get());
    }
    return 0;
  }

  @Override
  public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
    return 0;
  }

  @Override
  protected void implCloseSelectableChannel() throws IOException {

  }

  @Override
  protected void implConfigureBlocking(boolean block) throws IOException {

  }
}
