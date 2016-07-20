//
// EvhSetUserAccountInfoCommand.m
//
#import "EvhSetUserAccountInfoCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetUserAccountInfoCommand
//

@implementation EvhSetUserAccountInfoCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetUserAccountInfoCommand* obj = [EvhSetUserAccountInfoCommand new];
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
    if(self.accountName)
        [jsonObject setObject: self.accountName forKey: @"accountName"];
    if(self.password)
        [jsonObject setObject: self.password forKey: @"password"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.accountName = [jsonObject objectForKey: @"accountName"];
        if(self.accountName && [self.accountName isEqual:[NSNull null]])
            self.accountName = nil;

        self.password = [jsonObject objectForKey: @"password"];
        if(self.password && [self.password isEqual:[NSNull null]])
            self.password = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
