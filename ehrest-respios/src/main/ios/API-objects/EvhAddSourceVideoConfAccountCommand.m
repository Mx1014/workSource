//
// EvhAddSourceVideoConfAccountCommand.m
//
#import "EvhAddSourceVideoConfAccountCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddSourceVideoConfAccountCommand
//

@implementation EvhAddSourceVideoConfAccountCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddSourceVideoConfAccountCommand* obj = [EvhAddSourceVideoConfAccountCommand new];
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
    if(self.sourceAccount)
        [jsonObject setObject: self.sourceAccount forKey: @"sourceAccount"];
    if(self.password)
        [jsonObject setObject: self.password forKey: @"password"];
    if(self.accountCategory)
        [jsonObject setObject: self.accountCategory forKey: @"accountCategory"];
    if(self.validDate)
        [jsonObject setObject: self.validDate forKey: @"validDate"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sourceAccount = [jsonObject objectForKey: @"sourceAccount"];
        if(self.sourceAccount && [self.sourceAccount isEqual:[NSNull null]])
            self.sourceAccount = nil;

        self.password = [jsonObject objectForKey: @"password"];
        if(self.password && [self.password isEqual:[NSNull null]])
            self.password = nil;

        self.accountCategory = [jsonObject objectForKey: @"accountCategory"];
        if(self.accountCategory && [self.accountCategory isEqual:[NSNull null]])
            self.accountCategory = nil;

        self.validDate = [jsonObject objectForKey: @"validDate"];
        if(self.validDate && [self.validDate isEqual:[NSNull null]])
            self.validDate = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
