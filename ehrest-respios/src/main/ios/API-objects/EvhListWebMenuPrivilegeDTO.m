//
// EvhListWebMenuPrivilegeDTO.m
// generated at 2016-03-24 14:27:22 
//
#import "EvhListWebMenuPrivilegeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWebMenuPrivilegeDTO
//

@implementation EvhListWebMenuPrivilegeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListWebMenuPrivilegeDTO* obj = [EvhListWebMenuPrivilegeDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _dtos = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.moduleId)
        [jsonObject setObject: self.moduleId forKey: @"moduleId"];
    if(self.moduleName)
        [jsonObject setObject: self.moduleName forKey: @"moduleName"];
    if(self.dtos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
