//
// EvhPmsyPayerDTO.m
//
#import "EvhPmsyPayerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyPayerDTO
//

@implementation EvhPmsyPayerDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPmsyPayerDTO* obj = [EvhPmsyPayerDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userContact)
        [jsonObject setObject: self.userContact forKey: @"userContact"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userContact = [jsonObject objectForKey: @"userContact"];
        if(self.userContact && [self.userContact isEqual:[NSNull null]])
            self.userContact = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
