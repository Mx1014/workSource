//
// EvhSetEarlyWarningLineCommand.m
//
#import "EvhSetEarlyWarningLineCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetEarlyWarningLineCommand
//

@implementation EvhSetEarlyWarningLineCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetEarlyWarningLineCommand* obj = [EvhSetEarlyWarningLineCommand new];
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
    if(self.warningLine)
        [jsonObject setObject: self.warningLine forKey: @"warningLine"];
    if(self.warningLineType)
        [jsonObject setObject: self.warningLineType forKey: @"warningLineType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.warningLine = [jsonObject objectForKey: @"warningLine"];
        if(self.warningLine && [self.warningLine isEqual:[NSNull null]])
            self.warningLine = nil;

        self.warningLineType = [jsonObject objectForKey: @"warningLineType"];
        if(self.warningLineType && [self.warningLineType isEqual:[NSNull null]])
            self.warningLineType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
