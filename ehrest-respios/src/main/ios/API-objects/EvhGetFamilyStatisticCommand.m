//
// EvhGetFamilyStatisticCommand.m
//
#import "EvhGetFamilyStatisticCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetFamilyStatisticCommand
//

@implementation EvhGetFamilyStatisticCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetFamilyStatisticCommand* obj = [EvhGetFamilyStatisticCommand new];
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
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
