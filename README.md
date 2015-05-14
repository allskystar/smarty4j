Smarty4j
========

1.Smarty4j的使用
---------
```java
Engine engine = new Engine();
Template template = engine.getTemplate("demo.html");
template.merge(Context, Writer/OutputStream/Channel);
```
2.配置文件
---------
smarty.properties是对系统运行环境的配置，放到classes的根目录即可，默认值如下：
```shell
debug=true - 调试模式，模板文件更新将自动重新加载
cache=on - 启用cache，如果遇到变量无法刷新的问题，请关闭缓存(cache=off)
encoding=UTF-8 - 模板文件编码集
tpl.path=/ - 模板根路径
left.delimiter={ - 左边界定界符
right.delimiter=} - 右边界定界符
package.function=template.smarty4j.statement.function - 函数扩展包的名称，以:分隔
package.modifier=template.smarty4j.statement.modifier - 变量调节器扩展包的名称，以:分隔
```
3.Update(Version 1.1)
----------
* 针对OutputStream优化性能，重写了OutputStreamWriter的行为，取消了它内部使用的缓冲区，因为模板引擎在字节流与字符流中切换较频繁，且变量生存周期短(通常<1ms)，而缓冲区的创建操作需要消耗大量的资源(8096字节)
* 修复cycle,counter在include包含的模板中使用的死循环问题
* 优化变量缓存功能
	* 未使用的变量的赋值行为被取消(包括行函数的assign属性赋值行为)
	* foreach/section未使用的循环变量的赋值行为被取消
	* Function的execute方法提供返回值可用于assign的缓存赋值
	* include支持inline属性(取代原smarty4j 1.0中的macro函数)，不使用inline属性会触发变量缓存的强制回写
	* 请尽量避免使用eval函数，它会强制禁用缓存
	* 请减少在模板中使用函数中的assign属性或{capture}函数的使用，如果需要尽量在业务层完成，因为1.1版本已经是基于byte进行处理，如果使用这两个功能，这个片段的处理会转为基于String进行，影响效率
* 增加IContext接口，屏蔽掉get/set操作，如果一定需要使用可以强制转义成Context，请注意对变量缓存的影响，参见preventCache与preventAllCache方法
* 使用数据包装类型，减少循环setForeach对Map的操作次数
* 支持直接在函数属性值中使用变量调节器
* 支持针对强类型JavaBean的get操作的特殊操作符#，取代反射提高速度，在不使用操作符@时仍然使用弱类型反射的方式引用JavaBean属性。
如{$book.name}可以写成{$book.name#custom/Book}(假设存在custom.Book类，custom/Book是它的JVM名称)，你也可以使用{declare}函数来声明全局JavaBean名称，例如{declare var=book class="custom.Book"}或{declare var=book class="custom/Book"}
* 支持多行注释
* 修复section函数的bug
* 函数的赋值中可以直接应用公式与变量修饰器语法
* 修复操作符的一处定义错误(完全不影响使用)
* math函数能够自适应选择使用int型还是double型运算，``中包含的表达式使用double型运算，普通的表达式如果常量中有浮点数也使用double型运算，否则使用int型运算
* foreach支持smarty 3.x的语法，增加for函数的支持
* 赋值支持/{}直接定义数组和映射对象，如a=[1,2,3]或b={a:"test",b:false}
* 部分支持{$var = ...}的直接赋值语法，如{$a=10}
* 支持{block}函数，模板继承功能
* 为了减少缓存计算的复杂性，不支持标签的scope属性，部分场景可以使用call的非标准扩展属性return来实现，例如：{call ... return="a,b"}表示函数的a,b两个属性需要返回，smarty4j也不需要设置nocache属性，变量是不是要用cache由编译器来决定

