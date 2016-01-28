update eh_launch_pad_items set delete_flag=0 where item_group!='Bizs';
update eh_launch_pad_items set delete_flag=0 where item_group='Bizs' and item_label='更多';
update eh_launch_pad_items set delete_flag=0 where namespace_id=999999;