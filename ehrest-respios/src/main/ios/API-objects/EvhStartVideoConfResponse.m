//
// EvhStartVideoConfResponse.m
//
#import "EvhStartVideoConfResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhStartVideoConfResponse
//

@implementation EvhStartVideoConfResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhStartVideoConfResponse* obj = [EvhStartVideoConfResponse new];
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
    if(self.confHostId)
        [jsonObject setObject: self.confHostId forKey: @"confHostId"];
    if(self.confHostName)
        [jsonObject setObject: self.confHostName forKey: @"confHostName"];
    if(self.maxCount)
        [jsonObject setObject: self.maxCount forKey: @"maxCount"];
    if(self.token)
        [jsonObject setObject: self.token forKey: @"token"];
    if(self.meetingNo)
        [jsonObject setObject: self.meetingNo forKey: @"meetingNo"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confHostId = [jsonObject objectForKey: @"confHostId"];
        if(self.confHostId && [self.confHostId isEqual:[NSNull null]])
            self.confHostId = nil;

        self.confHostName = [jsonObject objectForKey: @"confHostName"];
        if(self.confHostName && [self.confHostName isEqual:[NSNull null]])
            self.confHostName = nil;

        self.maxCount = [jsonObject objectForKey: @"maxCount"];
        if(self.maxCount && [self.maxCount isEqual:[NSNull null]])
            self.maxCount = nil;

        self.token = [jsonObject objectForKey: @"token"];
        if(self.token && [self.token isEqual:[NSNull null]])
            self.token = nil;

        self.meetingNo = [jsonObject objectForKey: @"meetingNo"];
        if(self.meetingNo && [self.meetingNo isEqual:[NSNull null]])
            self.meetingNo = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
