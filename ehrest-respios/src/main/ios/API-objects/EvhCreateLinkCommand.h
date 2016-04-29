//
// EvhCreateLinkCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLinkCommand
//
@interface EvhCreateLinkCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* sourceType;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* author;

@property(nonatomic, copy) NSString* coverUri;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* contentAbstract;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

