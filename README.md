# Udger client for Java (data ver. 3)
Local parser is very fast and accurate useragent string detection solution. Enables developers to locally install and integrate a highly-scalable product.
We provide the detection of the devices (personal computer, tablet, Smart TV, Game console etc.), operating system and client SW type (browser, e-mail client etc.).
It also provides information about IP addresses (Public proxies, VPN services, Tor exit nodes, Fake crawlers, Web scrapers .. etc.)


- Tested with more the 50.000 unique user agents.
- Up to date data provided by https://udger.com/
- Support for >=Java6

### Compile from git repo

    $ git clone https://github.com/udger/udger-java
    $ cd udger-java/
    $ maven package

### Requirements
Udger data is stored in SQLite database file. Udger-java connects to SqLite using JDBC driver. SQLiteJDBC jdbc driver is recommended. If you are using Maven2, add the following XML fragments into your pom.xml file:

    <dependency>
      <groupId>org.xerial</groupId>
      <artifactId>sqlite-jdbc</artifactId>
      <version>3.8.11.2</version>
    </dependency>

### Usage

#### How to Specify Udger Database

Example how to create UdgerParser from udger db file `C:\work\udgerdb_v3.dat` (in Windows)

    UdgerParser up = = new UdgerParser("C:/work/udgerdb_v3.dat");


and from a UNIX (Linux, Mac OS X, etc) udger db file `/home/john/work/udgerdb_v3.dat`

    UdgerParser up = = new UdgerParser("/home/john/work/udgerdb_v3.dat");

#### Sample.java

```java
    public class Sample {
      public static void main(String[] args) {
        UdgerParser up = null;
        try {
            up = new UdgerParser("/home/john/work/udgerdb_v3.dat");
            UdgerUaResult uaRet = up.parseUa("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/601.3.9 (KHTML, like Gecko) Version/9.0.2 Safari/601.3.9");
            UdgerIpResult ipRet = up.parseIp("108.61.199.93");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.
            printStackTrace();
        } finally {
            if (up != null) {
                try {
                    up.close();
                } catch (IOException e) {
                }
            }
        }
      }
    }
```




### Author
The Udger.com Team (info@udger.com)

### old v1 format
If you still use the previous format of the db (v1), please see the branch old_format_v1
