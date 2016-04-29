//
// EvhDeleteQualityStandardCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
//
#import "EvhDeleteQualityStandardCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteQualityStandardCommand
//

@implementation EvhDeleteQualityStandardCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteQualityStandardCommand* obj = [EvhDeleteQualityStandardCommand new];
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
    if(self.standardId)
        [jsonObject setObject: self.standardId forKey: @"standardId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.standardId = [jsonObject objectForKey: @"standardId"];
        if(self.standardId && [self.standardId isEqual:[NSNull null]])
            self.standardId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
