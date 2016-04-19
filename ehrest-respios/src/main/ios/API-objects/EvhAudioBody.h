//
// EvhAudioBody.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAudioBody
//
@interface EvhAudioBody
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* url;

@property(nonatomic, copy) NSString* uri;

@property(nonatomic, copy) NSString* filename;

@property(nonatomic, copy) NSString* format;

@property(nonatomic, copy) NSNumber* fileSize;

@property(nonatomic, copy) NSString* duration;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

