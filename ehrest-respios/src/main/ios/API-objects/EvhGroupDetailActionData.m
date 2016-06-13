//
// EvhGroupDetailActionData.m
//
#import "EvhGroupDetailActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupDetailActionData
//

@implementation EvhGroupDetailActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGroupDetailActionData* obj = [EvhGroupDetailActionData new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.inviterId)
        [jsonObject setObject: self.inviterId forKey: @"inviterId"];
    if(self.privateFlag)
        [jsonObject setObject: self.privateFlag forKey: @"privateFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.inviterId = [jsonObject objectForKey: @"inviterId"];
        if(self.inviterId && [self.inviterId isEqual:[NSNull null]])
            self.inviterId = nil;

        self.privateFlag = [jsonObject objectForKey: @"privateFlag"];
        if(self.privateFlag && [self.privateFlag isEqual:[NSNull null]])
            self.privateFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
