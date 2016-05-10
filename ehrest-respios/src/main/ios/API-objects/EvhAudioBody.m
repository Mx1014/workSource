//
// EvhAudioBody.m
//
#import "EvhAudioBody.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAudioBody
//

@implementation EvhAudioBody

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAudioBody* obj = [EvhAudioBody new];
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
    if(self.url)
        [jsonObject setObject: self.url forKey: @"url"];
    if(self.uri)
        [jsonObject setObject: self.uri forKey: @"uri"];
    if(self.filename)
        [jsonObject setObject: self.filename forKey: @"filename"];
    if(self.format)
        [jsonObject setObject: self.format forKey: @"format"];
    if(self.fileSize)
        [jsonObject setObject: self.fileSize forKey: @"fileSize"];
    if(self.duration)
        [jsonObject setObject: self.duration forKey: @"duration"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.url = [jsonObject objectForKey: @"url"];
        if(self.url && [self.url isEqual:[NSNull null]])
            self.url = nil;

        self.uri = [jsonObject objectForKey: @"uri"];
        if(self.uri && [self.uri isEqual:[NSNull null]])
            self.uri = nil;

        self.filename = [jsonObject objectForKey: @"filename"];
        if(self.filename && [self.filename isEqual:[NSNull null]])
            self.filename = nil;

        self.format = [jsonObject objectForKey: @"format"];
        if(self.format && [self.format isEqual:[NSNull null]])
            self.format = nil;

        self.fileSize = [jsonObject objectForKey: @"fileSize"];
        if(self.fileSize && [self.fileSize isEqual:[NSNull null]])
            self.fileSize = nil;

        self.duration = [jsonObject objectForKey: @"duration"];
        if(self.duration && [self.duration isEqual:[NSNull null]])
            self.duration = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
