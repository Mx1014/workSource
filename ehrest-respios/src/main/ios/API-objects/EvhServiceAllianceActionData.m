//
// EvhServiceAllianceActionData.m
//
#import "EvhServiceAllianceActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhServiceAllianceActionData
//

@implementation EvhServiceAllianceActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhServiceAllianceActionData* obj = [EvhServiceAllianceActionData new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.type)
        [jsonObject setObject: self.type forKey: @"type"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.type = [jsonObject objectForKey: @"type"];
        if(self.type && [self.type isEqual:[NSNull null]])
            self.type = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
