//
// EvhVideoConfAccountTrialRuleDTO.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhVideoConfAccountTrialRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountTrialRuleDTO
//

@implementation EvhVideoConfAccountTrialRuleDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVideoConfAccountTrialRuleDTO* obj = [EvhVideoConfAccountTrialRuleDTO new];
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
    if(self.accounts)
        [jsonObject setObject: self.accounts forKey: @"accounts"];
    if(self.months)
        [jsonObject setObject: self.months forKey: @"months"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.accounts = [jsonObject objectForKey: @"accounts"];
        if(self.accounts && [self.accounts isEqual:[NSNull null]])
            self.accounts = nil;

        self.months = [jsonObject objectForKey: @"months"];
        if(self.months && [self.months isEqual:[NSNull null]])
            self.months = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
