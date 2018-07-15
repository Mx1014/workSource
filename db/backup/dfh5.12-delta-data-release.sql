-- 解决导入的时候费项名称多了*的bug by cx.yang
update eh_payment_bill_items set charging_item_name=REPLACE(charging_item_name,"*","");
update eh_payment_bill_items set charging_item_name=REPLACE(charging_item_name,"(元)","");