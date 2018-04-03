update eh_energy_meters m set m.bill_category_id = (select cs.id from eh_energy_meter_categories cs where cs.name = 
    (select c.name from eh_energy_meter_categories c where c.id = m.bill_category_id) 
    and cs.community_id = m.community_id and cs.category_type = 1);
update eh_energy_meters m set m.service_category_id = (select cs.id from eh_energy_meter_categories cs where cs.name = 
    (select c.name from eh_energy_meter_categories c where c.id = m.service_category_id) 
    and cs.community_id = m.community_id and cs.category_type = 2);
update eh_energy_meters m set m.cost_formula_id = (select fs.id from eh_energy_meter_formulas fs where fs.name = 
    (select f.name from eh_energy_meter_formulas f where f.id = m.cost_formula_id) 
    and fs.community_id = m.community_id and fs.formula_type = 2);
update eh_energy_meters m set m.amount_formula_id = (select fs.id from eh_energy_meter_formulas fs where fs.name = 
    (select f.name from eh_energy_meter_formulas f where f.id = m.amount_formula_id) 
    and fs.community_id = m.community_id and fs.formula_type = 1);