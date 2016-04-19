//
// EvhFavoriteBusinessesCommand.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFavoriteBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFavoriteBusinessesCommand
//
@interface EvhFavoriteBusinessesCommand
    : NSObject<EvhJsonSerializable>


// item type EvhFavoriteBusinessDTO*
@property(nonatomic, strong) NSMutableArray* bizs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

