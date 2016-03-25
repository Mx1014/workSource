//
// EvhCancelVideoConfCommand.m
// generated at 2016-03-25 15:57:22 
//
#import "EvhCancelVideoConfCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCancelVideoConfCommand
//

@implementation EvhCancelVideoConfCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCancelVideoConfCommand* obj = [EvhCancelVideoConfCommand new];
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
    if(self.confId)
        [jsonObject setObject: self.confId forKey: @"confId"];
    if(self.sourceAccountName)
        [jsonObject setObject: self.sourceAccountName forKey: @"sourceAccountName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confId = [jsonObject objectForKey: @"confId"];
        if(self.confId && [self.confId isEqual:[NSNull null]])
            self.confId = nil;

        self.sourceAccountName = [jsonObject objectForKey: @"sourceAccountName"];
        if(self.sourceAccountName && [self.sourceAccountName isEqual:[NSNull null]])
            self.sourceAccountName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
