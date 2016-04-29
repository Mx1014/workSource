//
// EvhGetLocalizedStringCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLocalizedStringCommand
//
@interface EvhGetLocalizedStringCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* scope;

@property(nonatomic, copy) NSString* code;

@property(nonatomic, copy) NSString* locale;

@property(nonatomic, copy) NSString* defaultValue;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

