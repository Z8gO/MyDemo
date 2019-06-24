package util;


 
 /**
 	* jdk 1.8 hashMap put 方法解析
  *来自： https://blog.csdn.net/zcw4237256/article/details/80684936		
 */
public class HashMap_put  {

  final V putVal(int hash, K key, V value, boolean onlyIfAbsent,boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 如果table为空，或者还没有元素时，则扩容
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 如果首结点值为空，则创建一个新的首结点。
    // 注意：(n - 1) & hash才是真正的hash值，也就是存储在table位置的index。在1.6中是封装成indexFor函数。
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {    // 到这儿了，就说明碰撞了，那么就要开始处理碰撞。
            Node<K,V> e; K k;
            // 如果在首结点与我们待插入的元素有相同的hash和key值，则先记录。
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode) // 如果首结点的类型是红黑树类型，则按照红黑树方法添加该元素
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {  // 到这一步，说明首结点类型为链表类型。
                    for (int binCount = 0; ; ++binCount) {
                        // 如果遍历到末尾时，先在尾部追加该元素结点。
                        if ((e = p.next) == null) {
                            p.next = newNode(hash, key, value, null);
                            // 当遍历的结点数目大于8时，则采取树化结构。
                            if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                                treeifyBin(tab, hash);
                                break;
                        }
                        // 如果找到与我们待插入的元素具有相同的hash和key值的结点，则停止遍历。此时e已经记录了该结点
                        if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                            break;
                        p = e;
                    }
                }
            // 表明，记录到具有相同元素的结点
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);  // 这个是空函数，可以由用户根据需要覆盖
                return oldValue;
            }
        }
    ++modCount;
    // 当结点数+1大于threshold时，则进行扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict); // 这个是空函数，可以由用户根据需要覆盖
    return null;
}

}
