//
// EvhCardUserDTO.m
//
#import "EvhCardUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardUserDTO
//

@implementation EvhCardUserDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCardUserDTO* obj = [EvhCardUserDTO new];
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
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.cardNo)
        [jsonObject setObject: self.cardNo forKey: @"cardNo"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.cardNo = [jsonObject objectForKey: @"cardNo"];
        if(self.cardNo && [self.cardNo isEqual:[NSNull null]])
            self.cardNo = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
