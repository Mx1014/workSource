//
// EvhFamilyDetailActionData.m
//
#import "EvhFamilyDetailActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyDetailActionData
//

@implementation EvhFamilyDetailActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFamilyDetailActionData* obj = [EvhFamilyDetailActionData new];
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
    if(self.inviterId)
        [jsonObject setObject: self.inviterId forKey: @"inviterId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        self.inviterId = [jsonObject objectForKey: @"inviterId"];
        if(self.inviterId && [self.inviterId isEqual:[NSNull null]])
            self.inviterId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
