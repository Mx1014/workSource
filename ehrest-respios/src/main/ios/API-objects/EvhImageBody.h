//
// EvhImageBody.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImageBody
//
@interface EvhImageBody
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* url;

@property(nonatomic, copy) NSString* uri;

@property(nonatomic, copy) NSString* thumbnail;

@property(nonatomic, copy) NSString* filename;

@property(nonatomic, copy) NSString* format;

@property(nonatomic, copy) NSNumber* fileSize;

@property(nonatomic, copy) NSNumber* width;

@property(nonatomic, copy) NSNumber* height;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

