//
// EvhLaunchPadLayoutGroupDTO.m
//
#import "EvhLaunchPadLayoutGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadLayoutGroupDTO
//

@implementation EvhLaunchPadLayoutGroupDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLaunchPadLayoutGroupDTO* obj = [EvhLaunchPadLayoutGroupDTO new];
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
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.widget)
        [jsonObject setObject: self.widget forKey: @"widget"];
    if(self.instanceConfig)
        [jsonObject setObject: self.instanceConfig forKey: @"instanceConfig"];
    if(self.style)
        [jsonObject setObject: self.style forKey: @"style"];
    if(self.defaultOrder)
        [jsonObject setObject: self.defaultOrder forKey: @"defaultOrder"];
    if(self.separatorFlag)
        [jsonObject setObject: self.separatorFlag forKey: @"separatorFlag"];
    if(self.separatorHeight)
        [jsonObject setObject: self.separatorHeight forKey: @"separatorHeight"];
    if(self.columnCount)
        [jsonObject setObject: self.columnCount forKey: @"columnCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.widget = [jsonObject objectForKey: @"widget"];
        if(self.widget && [self.widget isEqual:[NSNull null]])
            self.widget = nil;

        self.instanceConfig = [jsonObject objectForKey: @"instanceConfig"];
        if(self.instanceConfig && [self.instanceConfig isEqual:[NSNull null]])
            self.instanceConfig = nil;

        self.style = [jsonObject objectForKey: @"style"];
        if(self.style && [self.style isEqual:[NSNull null]])
            self.style = nil;

        self.defaultOrder = [jsonObject objectForKey: @"defaultOrder"];
        if(self.defaultOrder && [self.defaultOrder isEqual:[NSNull null]])
            self.defaultOrder = nil;

        self.separatorFlag = [jsonObject objectForKey: @"separatorFlag"];
        if(self.separatorFlag && [self.separatorFlag isEqual:[NSNull null]])
            self.separatorFlag = nil;

        self.separatorHeight = [jsonObject objectForKey: @"separatorHeight"];
        if(self.separatorHeight && [self.separatorHeight isEqual:[NSNull null]])
            self.separatorHeight = nil;

        self.columnCount = [jsonObject objectForKey: @"columnCount"];
        if(self.columnCount && [self.columnCount isEqual:[NSNull null]])
            self.columnCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
