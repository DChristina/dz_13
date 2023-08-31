package cc.robotdreams.automation.db;

import cc.robotdreams.automation.Session;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.text.StringEscapeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Category {
    static public class Item {
        final public int id;
        final public int parentId;
        final public String name;

        final private List<Item> childs = new LinkedList<>( ) {
        };

        public Item(int id, int parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        final public void addChild(Item child){
            if(child==null){
                throw new RuntimeException("Child can not be null");
            }
            this.childs.add(child);
        }
        final public List<Item> childs(){
            return new LinkedList<>( this.childs );
        }

    }

    public List<Item> getTopCategories() {
        List<Item> result = new LinkedList<>();

        ResultSet resultSet = Session.get( ).mysql( ).executeQuery(
                "SELECT " +
                        "oc_category.category_id, " +
                        "oc_category_description.name " +
                        "FROM oc_category " +
                        "INNER JOIN oc_category_description ON oc_category.category_id = oc_category_description.category_id " +
                        "WHERE oc_category_description.language_id = 1 AND oc_category.parent_id = 0 " +
                        "ORDER BY oc_category.sort_order ASC"
        );
        try {
            while (resultSet.next( )) {
                int categoryID = resultSet.getInt("category_id");
                String name = StringEscapeUtils.unescapeHtml4(resultSet.getString("name"));
                result.add(new Item(categoryID,0,name));
            }
        } catch (SQLException e) {

        }
        return result;
    }
    public List<Item> getAllCategories() {
        List<Item> result = new LinkedList<>(  );
        Map<Integer,Item> tmpList = new HashedMap(  );

        ResultSet resultSet = Session.get( ).mysql( ).executeQuery(
                "SELECT " +
                        "oc_category.category_id, " +
                        "oc_category.parent_id, " +
                        "oc_category_description.name " +
                        "FROM oc_category " +
                        "INNER JOIN oc_category_description ON oc_category.category_id = oc_category_description.category_id " +
                        "WHERE oc_category_description.language_id = 1 " +
                        "ORDER BY oc_category.sort_order ASC"
        );
        try {
            while (resultSet.next( )) {
                int categoryID = resultSet.getInt("category_id");
                int parentID = resultSet.getInt("parent_id");
                String name = StringEscapeUtils.unescapeHtml4(resultSet.getString("name"));
                tmpList.put(categoryID, new Item(categoryID,parentID,name));

            }
        } catch (SQLException e) {

        }
        for (Integer categoryId: tmpList.keySet()) {
            Item category = tmpList.get(categoryId);
            if (category.parentId==0){
                result.add(category);
            } else {
                Item parent = tmpList.get(category.parentId);
                parent.addChild(category);
            }
        }
        return result;
    }
}

